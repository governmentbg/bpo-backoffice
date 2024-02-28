package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.persistence.model.entity.user.CfGroupSecurityRole;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 16:46
 */
@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    public String toCore(CfGroupSecurityRole entity) {
        return entity.getSecurityRole().getRoleName();
    }

    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    @Mapping(target = "cfGroupSecurityRolePK.roleName", expression = "java(string)")
    @Mapping(target = "securityRole.roleName", expression = "java(string)")
    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(target = "creationUserId", expression = "java(bg.duosoft.ipas.util.security.SecurityUtils.getLoggedUserId())")
    public abstract CfGroupSecurityRole toEntity(String string);
}
