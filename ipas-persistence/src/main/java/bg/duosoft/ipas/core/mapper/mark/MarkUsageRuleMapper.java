package bg.duosoft.ipas.core.mapper.mark;


import bg.duosoft.ipas.core.model.mark.CMarkUsageRule;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkUsageRule;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        UsageRuleMapper.class})
public abstract class MarkUsageRuleMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "pk.id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "usageRule", source = "usageRule")
    @Mapping(target = "dateCreated", source = "dateCreated")
    @Mapping(target = "contentLoaded", expression = "java(loadFileContent)")
    @Mapping(target = "content", expression = "java(loadFileContent ? markUsageRule.getContent() : null)")
    public abstract CMarkUsageRule toCore(IpMarkUsageRule markUsageRule, @Context Boolean loadFileContent);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content", source = "content")
    @Mapping(target = "pk.type", source = "usageRule.id")
    public abstract IpMarkUsageRule toEntity(CMarkUsageRule cMarkUsageRule);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CMarkUsageRule> toCoreList(List<IpMarkUsageRule> ipMarkUsageRules, @Context Boolean loadFileContent);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpMarkUsageRule> toEntityList(List<CMarkUsageRule> cMarkUsageRules);
}
