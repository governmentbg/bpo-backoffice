package bg.duosoft.ipas.core.mapper.userdoc.grounds;

import bg.duosoft.ipas.core.model.userdoc.grounds.CEarlierRightTypes;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfEarlierRightTypes;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class EarlierRightTypesMapper {
    @Mapping(target = "id",  source = "id")
    @Mapping(target = "name",  source = "name")
    @BeanMapping(ignoreByDefault = true)
    public abstract CEarlierRightTypes toCore(CfEarlierRightTypes cfEarlierRightTypes);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfEarlierRightTypes toEntity(CEarlierRightTypes cEarlierRightTypes);
}
