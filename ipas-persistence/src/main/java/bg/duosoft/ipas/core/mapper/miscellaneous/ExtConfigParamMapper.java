package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.persistence.model.entity.ext.ExtConfigParam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ExtConfigParamMapper extends BaseObjectMapper<ExtConfigParam, CConfigParam> {

}
