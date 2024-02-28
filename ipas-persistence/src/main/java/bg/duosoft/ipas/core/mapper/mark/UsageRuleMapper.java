package bg.duosoft.ipas.core.mapper.mark;

import bg.duosoft.ipas.core.model.mark.CUsageRule;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUsageRule;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UsageRuleMapper {
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "name",  source = "name")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUsageRule toCore(CfUsageRule cfUsageRule);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfUsageRule toEntity(CUsageRule cUsageRule);
}
