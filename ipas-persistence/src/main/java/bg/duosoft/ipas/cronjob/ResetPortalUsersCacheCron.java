package bg.duosoft.ipas.cronjob;

import bg.duosoft.cronjob.cron.CronJobBase;
import bg.duosoft.cronjob.cron.CronJobExecutionException;
import bg.duosoft.cronjob.cron.CronJobExecutionResult;
import bg.duosoft.ipas.enums.UserGroupName;
import bg.duosoft.ipas.integration.portal.service.PortalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: ggeorgiev
 * Date: 24.01.2023
 * Time: 13:38
 */
@Component
@Slf4j
public class ResetPortalUsersCacheCron extends CronJobBase {
    private static AtomicBoolean running = new AtomicBoolean(false);
    @Autowired
    private PortalService portalService;
    @Override
    public CronJobExecutionResult execute() throws CronJobExecutionException {
        if (running.get()) {
            return new CronJobExecutionResult(-2);
        }
        try {
            running.set(true);
            portalService.resetCachedUsersOfGroupByGroupNameCache(UserGroupName.ROLE_TMEFILING_APPLICATION.getValue());
            return new CronJobExecutionResult(1);
        } finally {
            running.set(false);
        }

    }
}
