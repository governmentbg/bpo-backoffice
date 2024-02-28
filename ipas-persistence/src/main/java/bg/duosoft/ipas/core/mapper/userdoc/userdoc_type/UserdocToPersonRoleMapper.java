package bg.duosoft.ipas.core.mapper.userdoc.userdoc_type;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocTypeToPersonRole;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
        StringToBooleanMapper.class
})
public abstract class UserdocToPersonRoleMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "role", source = "pk.role")
    @Mapping(target = "indTakeFromOwner", source = "userdocPersonRole.indTakeFromOwner")
    @Mapping(target = "indTakeFromRepresentative", source = "userdocPersonRole.indTakeFromRepresentative")
    @Mapping(target = "indAdditionalOffidocCorrespondent", source = "userdocPersonRole.indAdditionalOffidocCorrespondent")
    @Mapping(target = "name", source = "userdocPersonRole.name")
    public abstract CUserdocPersonRole toCore(CfUserdocTypeToPersonRole cfUserdocTypeToPersonRole);

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    @Mapping(target = "userdocPersonRole.role", source = "role")
    public abstract CfUserdocTypeToPersonRole toEntity(CUserdocPersonRole cUserdocPersonRole);


}
