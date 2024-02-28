package bg.duosoft.ipas.cronjob.search;

import bg.duosoft.cronjob.cron.CronJobBase;
import bg.duosoft.cronjob.cron.CronJobExecutionException;
import bg.duosoft.cronjob.cron.CronJobExecutionResult;
import bg.duosoft.ipas.core.service.ext.IndexQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: ggeorgiev
 * Date: 07.04.2021
 * Time: 13:18
 */
@Component
@Slf4j
public class IncrementalIndexerCron extends CronJobBase {
    private static AtomicBoolean running = new AtomicBoolean(false);
    private static AtomicBoolean disabled = new AtomicBoolean(false);

    @Autowired
    private IndexQueueService indexQueueService;

    @Override
    public CronJobExecutionResult execute() throws CronJobExecutionException {
        if (running.get()) {
            return new CronJobExecutionResult(-2);
        }
        if (disabled.get()) {
            return new CronJobExecutionResult(-3);//disabled!
        }
        try {
            running.set(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            log.info("The index cron start time is {}", dateFormat.format(new Date()));
            indexQueueService.startIndexing();
            log.info("The index cron end time is {}", dateFormat.format(new Date()));
            return new CronJobExecutionResult(1);
        } finally {
            running.set(false);
        }
    }
    public static void disable() {
        LocalDateTime start = LocalDateTime.now();
        boolean r = running.get();
        while (r && LocalDateTime.now().minusSeconds(60).compareTo(start) < 0) {
            //wait...
            r = running.get();
        }
        if (r) {
            throw new RuntimeException("Cannot disable the IncrementalIndexerCron...It's still running");
        }

        log.info("Disabling the IncrementalIndexerCron");
        disabled.set(true);
    }
    public static void enable() {
        log.info("Enabling the IncrementalIndexerCron");
        disabled.set(false);
    }
}
