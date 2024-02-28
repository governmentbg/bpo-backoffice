package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraDataType;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocExtraDataType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserdocExtraDataTypeMapper extends BaseObjectMapper<CfUserdocExtraDataType, CUserdocExtraDataType> {

}