package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CSubStatusId;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfSubStatusPK;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubStatusIdMapper {

    @Mapping(source = "subStatusCode", target = "substatusCode")//TODO INTEGER
    @Mapping(source = "statusCode", target = "statusId.statusCode")
    @Mapping(source = "procTyp", target = "statusId.processType")
    @BeanMapping(ignoreByDefault = true)
    CSubStatusId toCore(CfSubStatusPK cfSubStatusPK);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    CfSubStatusPK toEntity(CSubStatusId cStatusId);


}
