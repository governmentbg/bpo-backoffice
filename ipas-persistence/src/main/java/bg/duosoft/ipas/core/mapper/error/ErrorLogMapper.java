package bg.duosoft.ipas.core.mapper.error;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.error.CErrorLog;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpErrorLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ErrorLogMapper extends BaseObjectMapper<IpErrorLog, CErrorLog> {

}
