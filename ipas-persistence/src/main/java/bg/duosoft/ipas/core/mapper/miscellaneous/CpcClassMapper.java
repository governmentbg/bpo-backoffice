package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassCpc;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class CpcClassMapper {
    @Mapping(target = "cpcEdition", source = "pk.cpcEditionCode")
    @Mapping(target = "cpcSection", source = "pk.cpcSectionCode")
    @Mapping(target = "cpcClass", source = "pk.cpcClassCode")
    @Mapping(target = "cpcSubclass", source = "pk.cpcSubclassCode")
    @Mapping(target = "cpcGroup", source = "pk.cpcGroupCode")
    @Mapping(target = "cpcSubgroup", source = "pk.cpcSubgroupCode")
    @Mapping(target = "cpcVersionCalculated", source = "cpcLatestVersion")
    @Mapping(target = "cpcName", source = "cpcName")
    @Mapping(target = "symbol", source = "symbol")
    @BeanMapping(ignoreByDefault = true)
    public abstract void toCore(CfClassCpc cfClassCpc, @MappingTarget CCpcClass cCpcClass);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    public abstract CfClassCpc toEntity(CCpcClass cCpcClass);
}
