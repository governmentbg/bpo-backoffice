package bg.duosoft.ipas.core.service.impl.ext;

import bg.duosoft.ipas.core.mapper.error.ErrorLogMapper;
import bg.duosoft.ipas.core.model.error.CErrorLog;
import bg.duosoft.ipas.core.service.ext.ErrorLogService;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpErrorLog;
import bg.duosoft.ipas.persistence.repository.entity.ext.IpErrorLogRepository;
import bg.duosoft.ipas.util.filter.ErrorLogFilter;
import bg.duosoft.ipas.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
public class ErrorLogServiceImpl implements ErrorLogService {

    @Autowired
    private IpErrorLogRepository ipErrorLogRepository;

    @Autowired
    private ErrorLogMapper errorLogMapper;

    @Override
    public List<CErrorLog> selectErrorLogs(ErrorLogFilter filter) {
        return ipErrorLogRepository.selectErrorLogs(filter);
    }

    @Override
    public int selectErrorLogsCount(ErrorLogFilter filter) {
        return ipErrorLogRepository.selectErrorLogsCount(filter);
    }

    @Override
    public CErrorLog selectById(Integer id) {
        if (Objects.isNull(id))
            return null;

        IpErrorLog ipErrorLog = ipErrorLogRepository.findById(id).orElse(null);
        if (Objects.isNull(ipErrorLog))
            return null;

        return errorLogMapper.toCore(ipErrorLog);
    }

    @Override
    public CErrorLog save(CErrorLog cErrorLog) {
        if (Objects.isNull(cErrorLog))
            return null;

        IpErrorLog ipErrorLog = errorLogMapper.toEntity(cErrorLog);
        IpErrorLog save = ipErrorLogRepository.save(ipErrorLog);
        return errorLogMapper.toCore(save);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CErrorLog createNewRecord(ErrorLogAbout about, String action, String errorMessage, String customMessage, boolean needManualFix, String instruction, ErrorLogPriority priority) {
        try {
            CErrorLog cErrorLog = CErrorLog.create(about, action, errorMessage, customMessage, needManualFix, instruction, priority, SecurityUtils.getLoggedUsername());
            return save(cErrorLog);
        } catch (Exception e1) {
            log.error("Cannot create new record in EXT_CORE.IP_ERROR_LOG table. Exception: " + e1.getMessage() + ". Error log message: " + errorMessage);
        }
        return null;
    }

    @Override
    public Integer countAllUnresolved() {
        return ipErrorLogRepository.countAllUnresolved();
    }

}
