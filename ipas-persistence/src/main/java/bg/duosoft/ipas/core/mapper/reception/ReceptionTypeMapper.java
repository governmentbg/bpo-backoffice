package bg.duosoft.ipas.core.mapper.reception;

import bg.duosoft.ipas.core.mapper.BaseObjectMapper;
import bg.duosoft.ipas.core.model.reception.CReceptionType;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.CfReceptionType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ReceptionTypeAdditionalUserdocMapper.class
})
public abstract class ReceptionTypeMapper extends BaseObjectMapper<CfReceptionType, CReceptionType> {

}
