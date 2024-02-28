package bg.duosoft.ipas.test.mapper.dailylog;

import bg.duosoft.ipas.core.mapper.dailylog.DailyLogMapper;
import bg.duosoft.ipas.core.model.dailylog.CDailyLog;
import bg.duosoft.ipas.core.model.dailylog.CDailyLogId;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLog;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLogPK;
import bg.duosoft.ipas.persistence.repository.entity.dailylog.DailyLogRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * User: Georgi
 * Date: 28.5.2020 г.
 * Time: 12:27
 */
public class DailyLogMapperTest extends TestBase {
    @Autowired
    private DailyLogMapper dailyLogMapper;

    @Test
    public void testMapDailyLog() {
        IpDailyLogPK pk = new IpDailyLogPK();
        pk.setDailyLogDate(new Date());
        pk.setDocLog("E");
        pk.setDocOri("BG");
        IpDailyLog dailyLog = new IpDailyLog();
        dailyLog.setAffectedFilesReadyDate(new Date());
        dailyLog.setArchivingDate(new Date());
        dailyLog.setCertificationReadyDate(new Date());
        dailyLog.setDigitalizationReadyDate(new Date());
        dailyLog.setFileCaptureReadyDate(new Date());
        dailyLog.setFirstDocNbr(1);
        dailyLog.setIndClosed("S");
        dailyLog.setIndOpen("N");
        dailyLog.setLastDocNbr(5);
        dailyLog.setLogoCaptureReadyDate(new Date());
        dailyLog.setRowVersion(1);
        dailyLog.setPk(pk);
        dailyLog.setUserdocCaptureReadyDate(new Date());
        dailyLog.setViennaCodesReadyDate(new Date());
        CDailyLog core = dailyLogMapper.toCore(dailyLog);
        IpDailyLog transformed = dailyLogMapper.toEntity(core);
        _assertEquals(dailyLog, transformed, IpDailyLog::getAffectedFilesReadyDate);
        _assertEquals(dailyLog, transformed, IpDailyLog::getArchivingDate);
        _assertEquals(dailyLog, transformed, IpDailyLog::getCertificationReadyDate);
        _assertEquals(dailyLog, transformed, IpDailyLog::getDigitalizationReadyDate);
        _assertEquals(dailyLog, transformed, IpDailyLog::getFileCaptureReadyDate);
        _assertEquals(dailyLog, transformed, IpDailyLog::getFirstDocNbr);
        _assertEquals(dailyLog, transformed, IpDailyLog::getIndClosed);
        _assertEquals(dailyLog, transformed, IpDailyLog::getIndOpen);
        _assertEquals(dailyLog, transformed, IpDailyLog::getLastDocNbr);
        _assertEquals(dailyLog, transformed, IpDailyLog::getLogoCaptureReadyDate);
        _assertEquals(dailyLog, transformed, IpDailyLog::getRowVersion);
        _assertEquals(dailyLog.getPk(), transformed.getPk(), IpDailyLogPK::getDailyLogDate);
        _assertEquals(dailyLog.getPk(), transformed.getPk(), IpDailyLogPK::getDocLog);
        _assertEquals(dailyLog.getPk(), transformed.getPk(), IpDailyLogPK::getDocOri);
        _assertEquals(dailyLog, transformed, IpDailyLog::getUserdocCaptureReadyDate);
        _assertEquals(dailyLog, transformed, IpDailyLog::getViennaCodesReadyDate);
    }


}
