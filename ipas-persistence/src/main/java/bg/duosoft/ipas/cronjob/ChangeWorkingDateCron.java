package bg.duosoft.ipas.cronjob;


import bg.duosoft.cronjob.cron.CronJobBase;
import bg.duosoft.cronjob.cron.CronJobExecutionException;
import bg.duosoft.cronjob.cron.CronJobExecutionResult;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.dailylog.DailyLogServiceException;
import bg.duosoft.ipas.util.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Component
public class ChangeWorkingDateCron extends CronJobBase {

    @Autowired
    private DailyLogService dailyLogService;


    private void changeIpasWorkingDate() throws DailyLogServiceException {
        Date date = DateUtils.convertToDate(LocalDate.now());
        log.debug("Trying to change working date to: " + date);
        dailyLogService.changeWorkingDate(date);
        log.debug("End of calling changeWorkingDate");
    }
    private void performCloseDailyLogChecks() throws DailyLogServiceException {
        log.debug("Calling performCloseDailyLogChecks");
        dailyLogService.performCloseDailyLogChecks();
        log.debug("End of calling performCloseDailyLogChecks");
    }

    @Override
    public CronJobExecutionResult execute() throws CronJobExecutionException {
        log.info("IPAS change working date cron start time is {}", DateUtils.DATE_TIME_FORMATTER.format(new Date()));

        String warnings = null;
        //ako daily log-a ne moje da se zatvori se hvyrlq exception
        try {
            changeIpasWorkingDate();
        } catch (DailyLogServiceException e) {
            log.error("Exception changing working date", e);
            throw new CronJobExecutionException(e);
        }
        //ako ne mogat da se izpylnqt daily log checks, se dobavq warning, a ne se hvyrlq exception!!!
        try {
            performCloseDailyLogChecks();
        } catch (DailyLogServiceException e) {
            log.warn("Exception performing daily log checks", e);
            warnings = "Cannot perform daily log checks\nException:" + ExceptionUtils.getFullStackTrace(e);
        }
        log.info("IPAS change working date end time is {}", DateUtils.DATE_TIME_FORMATTER.format(new Date()));
        return new CronJobExecutionResult(1, warnings);
    }
}
