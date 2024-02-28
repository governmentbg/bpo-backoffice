package bg.duosoft.ipas.core.mapper.offidoc;

import bg.duosoft.ipas.core.model.offidoc.COffidocTypeStaticTemplate;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfOffidocTypeStaticTemplate;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring")
public abstract class OffidocTypeStaticTemplateMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "offidocType", source = "pk.offidocTyp")
    @Mapping(target = "staticFileName", source = "pk.staticFileName")
    public abstract COffidocTypeStaticTemplate toCore(CfOffidocTypeStaticTemplate entity);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfOffidocTypeStaticTemplate toEntity(COffidocTypeStaticTemplate core);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<COffidocTypeStaticTemplate> toCoreList(List<CfOffidocTypeStaticTemplate> entityList);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfOffidocTypeStaticTemplate> toEntityList(List<COffidocTypeStaticTemplate> coreList);
}
