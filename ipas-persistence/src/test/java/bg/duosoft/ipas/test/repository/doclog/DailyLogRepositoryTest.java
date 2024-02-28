package bg.duosoft.ipas.test.repository.doclog;

import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLog;
import bg.duosoft.ipas.persistence.repository.entity.dailylog.DailyLogRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.DefaultValue;
import bg.duosoft.ipas.util.date.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: Georgi
 * Date: 27.5.2020 г.
 * Time: 18:05
 */
public class DailyLogRepositoryTest extends TestBase {
    @Autowired
    private DailyLogRepository dailyLogRepository;
    @Test
    public void testGetOpenedDailyLogDate() {
        Optional<IpDailyLog> openedDailyLog = dailyLogRepository.getOpenedDailyLog(DefaultValue.DEFAULT_DOC_ORI);
        assertTrue(openedDailyLog.isPresent());
        assertEquals(LocalDate.of(2019, 2, 8), DateUtils.convertToLocalDate(openedDailyLog.get().getPk().getDailyLogDate()));
    }
}
