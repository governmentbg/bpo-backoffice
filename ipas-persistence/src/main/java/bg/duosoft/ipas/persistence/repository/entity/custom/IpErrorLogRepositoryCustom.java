package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.core.model.error.CErrorLog;
import bg.duosoft.ipas.util.filter.ErrorLogFilter;

import java.util.List;

public interface IpErrorLogRepositoryCustom {

    List<CErrorLog> selectErrorLogs(ErrorLogFilter filter);

    int selectErrorLogsCount(ErrorLogFilter filter);
}
