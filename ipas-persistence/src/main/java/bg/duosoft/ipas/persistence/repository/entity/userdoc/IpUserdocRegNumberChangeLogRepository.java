package bg.duosoft.ipas.persistence.repository.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRegNumberChangeLog;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpUserdocRegNumberChangeLogRepositoryCustom;

public interface IpUserdocRegNumberChangeLogRepository extends BaseRepository<IpUserdocRegNumberChangeLog, Integer>, IpUserdocRegNumberChangeLogRepositoryCustom {

}
