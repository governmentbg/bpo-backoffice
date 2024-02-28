package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type;

import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypes;
import org.mapstruct.*;

import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class UserdocTypeMapper extends BaseUserdocTypeMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "userdocType", source = "pk.userdocTyp")
    @Mapping(target = "userdocName", source = "userdocType.userdocName")
    @Mapping(target = "generateProcTyp", source = "userdocType.generateProcTyp")
    public abstract CUserdocType toCore(IpUserdocTypes ipUserdocTypes);

    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    @Mapping(target = "userdocType.userdocTyp", source = "userdocType")
    @Mapping(target = "userdocType.generateProcTyp", source = "generateProcTyp")
    public abstract IpUserdocTypes toEntity(CUserdocType cUserdocType);

    @AfterMapping
    protected void afterToCore(@MappingTarget CUserdocType target, IpUserdocTypes source) {
        CfUserdocType userdocType = source.getUserdocType();
        if (Objects.nonNull(userdocType)) {
            afterToCore(target, userdocType);
        }
    }

}
