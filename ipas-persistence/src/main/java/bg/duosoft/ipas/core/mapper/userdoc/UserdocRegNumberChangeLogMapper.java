package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocRegNumberChangeLog;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRegNumberChangeLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserdocRegNumberChangeLogMapper extends BaseObjectMapper<IpUserdocRegNumberChangeLog, CUserdocRegNumberChangeLog> {

}
