package bg.duosoft.ipas.persistence.repository.entity.custom;

import bg.duosoft.ipas.persistence.model.nonentity.SimpleUserdocRegNumberChangeLog;
import bg.duosoft.ipas.util.filter.UserdocRegNumberChangeLogFilter;

import java.util.List;

public interface IpUserdocRegNumberChangeLogRepositoryCustom {
    List<SimpleUserdocRegNumberChangeLog> getSimpleUserdocRegNumberChangeLogList(UserdocRegNumberChangeLogFilter filter);

    Integer getSimpleUserdocRegNumberChangeLogCount(UserdocRegNumberChangeLogFilter filter);
}
