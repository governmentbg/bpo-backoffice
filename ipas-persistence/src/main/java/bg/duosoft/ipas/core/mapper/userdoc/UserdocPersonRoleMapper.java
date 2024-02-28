package bg.duosoft.ipas.core.mapper.userdoc;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocPersonRole;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StringToBooleanMapper.class})
public abstract class UserdocPersonRoleMapper {

    @Mapping(source = "role", target = "role")
    @Mapping(source = "indTakeFromOwner", target = "indTakeFromOwner")
    @Mapping(source = "indTakeFromRepresentative", target = "indTakeFromRepresentative")
    @Mapping(source = "indAdditionalOffidocCorrespondent", target = "indAdditionalOffidocCorrespondent")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    public abstract CUserdocPersonRole toCore(CfUserdocPersonRole cfUserdocPersonRole);

    @InheritInverseConfiguration
    @BeanMapping(ignoreByDefault = true)
    public abstract CfUserdocPersonRole toEntity(CUserdocPersonRole userdocPersonRole);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CfUserdocPersonRole> toEntityList(List<CUserdocPersonRole> userdocPersonRoles);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<CUserdocPersonRole> toCoreList(List<CfUserdocPersonRole> cfUserdocPersonRoles);


}
