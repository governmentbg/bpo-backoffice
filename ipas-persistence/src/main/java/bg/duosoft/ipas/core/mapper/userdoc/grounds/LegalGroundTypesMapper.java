package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CLegalGroundTypes;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLegalGroundTypes;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LegalGroundTypesMapper {

    @Mapping(target = "id",  source = "id")
    @Mapping(target = "title",  source = "title")
    @Mapping(target = "description",  source = "description")
    @BeanMapping(ignoreByDefault = true)
    public abstract CLegalGroundTypes toCore(CfLegalGroundTypes cfLegalGroundType);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfLegalGroundTypes toEntity(CLegalGroundTypes cLegalGroundType);
}
