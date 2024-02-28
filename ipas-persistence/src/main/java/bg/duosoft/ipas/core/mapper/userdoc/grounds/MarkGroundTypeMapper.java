package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CMarkGroundType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfMarkGroundType;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class MarkGroundTypeMapper {
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "name",  source = "name")
    @BeanMapping(ignoreByDefault = true)
    public abstract CMarkGroundType toCore(CfMarkGroundType cfMarkGroundType);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfMarkGroundType toEntity(CMarkGroundType cMarkGroundType);
}
