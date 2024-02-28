package bg.duosoft.ipas.core.mapper.miscellaneous;

import bg.duosoft.ipas.core.model.design.CLocarnoClasses;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassLocarno;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class LocarnoClassesMapper {

    @Mapping(target = "locarnoClassCode", source = "pk.locarnoClassCode")
    @Mapping(target = "locarnoEditionCode", source = "pk.locarnoEditionCode")
    @Mapping(target = "locarnoName", source = "locarnoName")
    @BeanMapping(ignoreByDefault = true)
    public abstract CLocarnoClasses toCore(CfClassLocarno cfClassLocarno);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "rowVersion", constant = "1")
    public abstract CfClassLocarno toEntity(CLocarnoClasses cLocarnoClasses);
}


