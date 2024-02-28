package bg.duosoft.ipas.core.mapper.offidoc;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.offidoc.COffidocType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfOffidocType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
    uses = {
            OffidocTypeTemplateMapper.class,
            OffidocTypeStaticTemplateMapper.class,
            StringToBooleanMapper.class
    })
public abstract class OffidocTypeMapper {

    @Mapping(source = "offidocTyp", target = "offidocType")
    @Mapping(source = "offidocName", target = "offidocName")
    @Mapping(source = "indFlag1", target = "direction")
    @Mapping(source = "indFlag2", target = "hasPublication")
    @Mapping(source = "nameWfile", target = "defaultTemplate")
    @Mapping(source = "templates", target = "templates")
    @Mapping(source = "staticTemplates", target = "staticTemplates")
    @BeanMapping(ignoreByDefault = true)
    public abstract COffidocType toCore(CfOffidocType cfOffidocType);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfOffidocType toEntity(COffidocType cOffidocType);

    @InheritConfiguration(name = "toEntity")
    public abstract void fillEntityFields(COffidocType source, @MappingTarget CfOffidocType target);
    
    @BeanMapping(ignoreByDefault = true)
    public abstract List<COffidocType> toCoreList(List<CfOffidocType> cfOffidocTypes);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    public abstract List<CfOffidocType> toEntityList(List<COffidocType> cOffidocTypes);

}
