package bg.duosoft.ipas.core.service.ext;

import bg.duosoft.ipas.core.model.error.CErrorLog;
import bg.duosoft.ipas.enums.ErrorLogAbout;
import bg.duosoft.ipas.enums.ErrorLogPriority;
import bg.duosoft.ipas.util.filter.ErrorLogFilter;

import java.util.List;

public interface ErrorLogService {

    List<CErrorLog> selectErrorLogs(ErrorLogFilter filter);

    int selectErrorLogsCount(ErrorLogFilter filter);

    CErrorLog selectById(Integer id);

    CErrorLog save(CErrorLog cErrorLog);

    CErrorLog createNewRecord(ErrorLogAbout about, String action, String errorMessage, String customMessage, boolean needManualFix, String instruction, ErrorLogPriority priority);

    Integer countAllUnresolved();

}
