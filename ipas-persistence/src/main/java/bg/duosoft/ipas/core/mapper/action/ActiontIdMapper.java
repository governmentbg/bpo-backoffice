package bg.duosoft.ipas.core.mapper.action;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActiontIdMapper {

    @Mapping(source = "procNbr", target = "processId.processNbr")
    @Mapping(source = "procTyp", target = "processId.processType")
    @Mapping(source = "actionNbr", target = "actionNbr")
    @BeanMapping(ignoreByDefault = true)
    CActionId toCore(IpActionPK ipActionPK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    IpActionPK toEntity(CActionId cActionId);

}
