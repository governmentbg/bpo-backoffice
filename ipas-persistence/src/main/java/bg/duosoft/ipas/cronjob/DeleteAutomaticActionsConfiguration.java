package bg.duosoft.ipas.cronjob;

import bg.duosoft.cronjob.cron.CronJobBase;
import bg.duosoft.cronjob.cron.CronJobExecutionException;
import bg.duosoft.cronjob.cron.CronJobExecutionResult;
import bg.duosoft.ipas.core.service.nomenclature.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: ggeorgiev
 * Date: 06.03.2023
 * Time: 10:45
 */
@Component
public class DeleteAutomaticActionsConfiguration extends CronJobBase {
    @Autowired
    private ConfigurationService configurationService;
    @Override
    public CronJobExecutionResult execute() throws CronJobExecutionException {
        configurationService.deleteConfiguration(ConfigurationService.AUTOMATIC_ACTION_24H);
        return new CronJobExecutionResult(1);
    }
}
