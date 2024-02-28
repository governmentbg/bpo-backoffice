package bg.duosoft.ipas.test.service.dailylog;

import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.dailylog.DailyLogServiceException;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Georgi
 * Date: 25.9.2020 г.
 * Time: 13:58
 */
public class DailyLogServiceTest extends TestBase {
    @Autowired
    private DailyLogService dailyLogService;
    @Test
    @Ignore 
    public void testPerformDailyLogChecks() {
        try {
            dailyLogService.performCloseDailyLogChecks();
        } catch (DailyLogServiceException e) {
            e.printStackTrace();
        }
    }
}
