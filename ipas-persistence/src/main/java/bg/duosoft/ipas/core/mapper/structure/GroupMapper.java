package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.model.structure.Group;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisGroup;
import org.mapstruct.*;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 16:46
 */
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class GroupMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "description",    source = "description")
    @Mapping(target = "groupName",      source = "groupname")
    @Mapping(target = "groupId",        source = "groupId")
    @Mapping(target = "roleNames",      source = "groupSecurityRoles")
    public abstract Group toCore(CfThisGroup entity);


    @BeanMapping(ignoreByDefault = true)
    @InheritInverseConfiguration
    @Mapping(target = "rowVersion", constant = "1")
    public abstract CfThisGroup toEntity(Group core);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(ignoreByDefault = true)
    public abstract void fillEntityBean(Group source, @MappingTarget CfThisGroup target);

    @AfterMapping
    public void afterToEntityMapping(Group source, @MappingTarget CfThisGroup target) {
        if (target.getGroupSecurityRoles() != null) {
            target.getGroupSecurityRoles().forEach(sr -> sr.getCfGroupSecurityRolePK().setGroupId(source.getGroupId()));
        }

    }
}
