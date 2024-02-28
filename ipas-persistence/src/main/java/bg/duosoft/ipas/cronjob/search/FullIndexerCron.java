package bg.duosoft.ipas.cronjob.search;

import bg.duosoft.cronjob.cron.CronJobBase;
import bg.duosoft.cronjob.cron.CronJobExecutionException;
import bg.duosoft.cronjob.cron.CronJobExecutionResult;
import bg.duosoft.ipas.core.service.ext.IndexQueueService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.util.search.EntityIndexProgressMonitor;
import bg.duosoft.ipas.util.search.IndexProgressMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 07.04.2021
 * Time: 13:28
 */
@Component
@Slf4j
public class FullIndexerCron extends CronJobBase {
    private static final int RUNNING_TIME_SECONDS = 60 * 60;

    @Autowired
    private IndexService indexService;

    @Autowired
    private IndexQueueService indexQueueService;

    private static AtomicBoolean running = new AtomicBoolean(false);

    @Override
    public CronJobExecutionResult execute() throws CronJobExecutionException {
        if (running.get()) {
            return new CronJobExecutionResult(-2);
        }

        try {
            running.set(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            log.info("The index cron start time is {}", dateFormat.format(new Date()));
            IncrementalIndexerCron.disable();
            Optional<Integer> maxQueueId = indexQueueService.getQueueMaxId();
            IndexResult res = fullIndex();
            if (!res.hasError && maxQueueId.isPresent()) {
                emptyQueue(res, maxQueueId.get());
            }

            if (res.hasError) {
                log.error("Error executing full indexer\n" + res.result);
                throw new CronJobExecutionException(res.getResult());
            }

            log.info("The index cron end time is {}", dateFormat.format(new Date()));
            return new CronJobExecutionResult(1, res.getResult());

        } finally {
            running.set(false);
            IncrementalIndexerCron.enable();
        }



    }
    private void emptyQueue(IndexResult res, int id) {
        try {
            indexQueueService.deleteQueueOldRecords(id);
        } catch (Exception e) {
            res.setError(true);
            res.result.add("Error emptying queue: " + ExceptionUtils.getStackTrace(e));
        }
    }
    private class IndexResult {
        private boolean hasError;
        private List<String> result = new ArrayList<>();
        private IndexResult(String res, boolean hasError) {
            if (!StringUtils.isEmpty(res)) {
                this.result.add(res);
            }
            this.hasError = hasError;
        }
        private void setError(boolean error) {
            this.hasError = error;
        }
        private void addResult(String result) {
            this.result.add(result);
        }
        private String getResult() {
            return result.stream().collect(Collectors.joining("\n"));
        }
    }
    public IndexResult fullIndex() {
        
        IndexProgressMonitor monitor = indexService.indexAll(false);
        int cnt = 1;
        StopWatch sw = new StopWatch();
        sw.start();
        LocalDateTime start = LocalDateTime.now();

        //chaka se ili dokato ima nqkoi monitor koito vyrvi ili dokato ne mine RINNING_TIME_SECONDS
        while (monitor.isRunning() && LocalDateTime.now().isBefore(start.plusSeconds(RUNNING_TIME_SECONDS))) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.warn("Exception while sleeping", e);
            }
            cnt++;
            if (cnt % 30 == 0) {
                sw.stop();
                log.info("Indexing....Time elapsed " + sw.getTotalTimeMillis() + " ms. Not finished yet...");
                sw.start();
            }
        }
        sw.stop();

        boolean hasError = !StringUtils.isEmpty(monitor.getGlobalError()) || monitor.getEntityProgressMonitors().stream().map(r -> r.getError()).filter(StringUtils::isNotEmpty).findAny().isPresent() || monitor.isRunning();

        String res = monitor
                .getEntityProgressMonitors()
                .stream()
                .map(m -> hasError ? toString(m) : toStringShort(m))
                .collect(Collectors.joining("\n"));
        if (!StringUtils.isEmpty(monitor.getGlobalError())) {
            res += "\nGlobalError: " + monitor.getGlobalError();
        }
        if (monitor.isRunning()) {
            res += "\nERROR: THE INDEXER DID NOT FINISH IN " + sw.getTotalTimeSeconds() + " seconds!!!!!";
        }



        return new IndexResult(res, hasError);
    }
    private static String toString(EntityIndexProgressMonitor m) {
        StringBuilder b = new StringBuilder(m.getEntityType().getName());
        b.append("\nTotalRecords: " + m.getTotalCounter().longValue());
        b.append("\nProcessedRecords: " + m.getDocumentsDoneCounter().longValue());
        if (!StringUtils.isEmpty(m.getError())) {
            b.append("\nError: " + m.getError());
        }
        b.append("\nFinished: " + !m.isRunning());
        b.append("\n----------------------");
        return b.toString();

    }
    private static String toStringShort(EntityIndexProgressMonitor m) {
        StringBuilder b = new StringBuilder(m.getEntityType().getName() + "\t Indexed Records: " + m.getDocumentsDoneCounter().longValue() + " out of " + m.getTotalCounter().longValue() + "\tFinished:" + !m.isRunning());
        return b.toString();
    }
}
