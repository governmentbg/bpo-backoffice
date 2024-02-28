package bg.duosoft.ipas.core.service.impl.dailylog;

import bg.duosoft.ipas.core.mapper.dailylog.DailyLogMapper;
import bg.duosoft.ipas.core.model.dailylog.CDailyLog;
import bg.duosoft.ipas.core.service.dailylog.DailyLogService;
import bg.duosoft.ipas.core.service.dailylog.DailyLogServiceException;
import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLog;
import bg.duosoft.ipas.persistence.repository.entity.dailylog.DailyLogRepository;
import bg.duosoft.ipas.services.core.IpasDailyLogService;
import bg.duosoft.ipas.services.core.IpasServiceException;
import bg.duosoft.ipas.util.DefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class DailyLogServiceImpl implements DailyLogService {

    @Autowired
    private IpasDailyLogService ipasDailyLogService;

    @Autowired
    private DailyLogRepository dailyLogRepository;
    @Autowired
    private DailyLogMapper dailyLogMapper;
    @Override
    @CacheEvict(value = "dailyLog", allEntries = true)
    public void changeWorkingDate(Date date) throws DailyLogServiceException {
        try {
            ipasDailyLogService.openDailyLog(date);//TODO:Implement it and remove the ipasDailyLogService dependency!!!
        } catch (Exception e) {
            throw new DailyLogServiceException(e);
        }
    }
    public void performCloseDailyLogChecks() throws DailyLogServiceException {
        try {
            ipasDailyLogService.performCloseDailyLogChecks();
        } catch (Exception e) {
            throw new DailyLogServiceException(e);
        }
    }

    @Override
    @Cacheable(value = "dailyLog", key = "{'workingDate'}")
    public Date getWorkingDate() {
        Optional<IpDailyLog> openedDailyLog = dailyLogRepository.getOpenedDailyLog(DefaultValue.DEFAULT_DOC_ORI);
        return openedDailyLog.isPresent() ? openedDailyLog.get().getPk().getDailyLogDate() : null;
    }
    @Cacheable(value = "dailyLog", key = "{'openedDailyLog', #docOri}")
    public CDailyLog getOpenedDailyLog(String docOri) {
        Optional<IpDailyLog> openedDailyLog = dailyLogRepository.getOpenedDailyLog(docOri);
        return openedDailyLog.isEmpty() ? null : dailyLogMapper.toCore(openedDailyLog.get());
    }

}
