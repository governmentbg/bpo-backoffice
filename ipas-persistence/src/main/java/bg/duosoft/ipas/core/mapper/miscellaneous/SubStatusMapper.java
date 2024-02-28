package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CSubstatus;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfSubStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        SubStatusIdMapper.class
})
public abstract class SubStatusMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "pk", target = "subStatus")
    @Mapping(source = "subStatusName", target = "substatusName")
    public abstract CSubstatus toCore(CfSubStatus cfSubStatus);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfSubStatus toEntity(CSubstatus cSubstatus);
}
