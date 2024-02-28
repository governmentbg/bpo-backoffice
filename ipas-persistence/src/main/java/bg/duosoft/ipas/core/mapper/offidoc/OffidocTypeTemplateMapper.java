package bg.duosoft.ipas.core.mapper.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeTemplate;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring", uses = {
        OffidocTypeTemplateConfigMapper.class
})
public abstract class OffidocTypeTemplateMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "offidocType", source = "pk.offidocTyp")
    @Mapping(target = "nameWFile", source = "pk.nameWfile")
    @Mapping(target = "nameFileConfig", source = "nameFileConfig")
    public abstract COffidocTypeTemplate toCore(CfOffidocTypeTemplate cfOffidocTypeTemplate);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfOffidocTypeTemplate toEntity(COffidocTypeTemplate cOffidocTypeTemplate);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<COffidocTypeTemplate> toCoreList(List<CfOffidocTypeTemplate> offidocTypeTemplates);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfOffidocTypeTemplate> toEntityList(List<COffidocTypeTemplate> templates);
}
