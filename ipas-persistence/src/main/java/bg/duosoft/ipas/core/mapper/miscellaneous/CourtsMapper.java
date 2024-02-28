package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CCourt;
import bg.duosoft.ipas.persistence.model.entity.ext.legal.Courts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CourtsMapper extends BaseObjectMapper<Courts, CCourt> {

}
