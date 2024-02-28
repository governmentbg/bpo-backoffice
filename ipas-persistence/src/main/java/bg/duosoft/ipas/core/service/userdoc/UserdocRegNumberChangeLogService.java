package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.userdoc.CUserdocRegNumberChangeLog;
import bg.duosoft.ipas.persistence.model.nonentity.SimpleUserdocRegNumberChangeLog;
import bg.duosoft.ipas.util.filter.UserdocRegNumberChangeLogFilter;

import java.util.List;

public interface UserdocRegNumberChangeLogService {

    CUserdocRegNumberChangeLog save(CUserdocRegNumberChangeLog log);

    List<SimpleUserdocRegNumberChangeLog> getUserdocRegNumberChangeLogList(UserdocRegNumberChangeLogFilter filter);

    Integer getUserdocRegNumberChangeLogCount(UserdocRegNumberChangeLogFilter filter);
}
