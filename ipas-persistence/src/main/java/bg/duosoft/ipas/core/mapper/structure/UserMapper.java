package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(componentModel = "spring",
uses = {
        UserGroupToGroupIdMapper.class,
        StringToBooleanMapper.class
})
public abstract class UserMapper {

    @Mapping(target = "userId",                 source = "userId")
    @Mapping(target = "userName",               source = "userName")
    @Mapping(target = "login",                  source = "login")
    @Mapping(target = "indAdministrator",       source = "indAdministrator")
    @Mapping(target = "indExaminer",            source = "indExaminer")
    @Mapping(target = "indInactive",            source = "indInactive")
    @Mapping(target = "indExternal",            source = "indExternal")
    @Mapping(target = "initials",               source = "initials")
//    @Mapping(target = "loginPassword",          source = "loginPassword")
    @Mapping(target = "email",                  source = "email")
    @Mapping(target = "telephone",              source = "telephone")
    @Mapping(target = "fullName",               source = "fullName")
//    @Mapping(target = "personalId",             source = "personalId")
//    @Mapping(target = "signatureTyp",           source = "signatureTyp")
//    @Mapping(target = "signatureData",          source = "signatureData")
    @Mapping(target = "officeSectionCode",      source = "officeSectionCode")
    @Mapping(target = "officeDivisionCode",     source = "officeDivisionCode")
    @Mapping(target = "officeDepartmentCode",   source = "officeDepartmentCode")
    @Mapping(target = "groupIds",               source = "userGroups")
    @BeanMapping(ignoreByDefault = true)
    public abstract User toCore(IpUser ipUser);



    @InheritInverseConfiguration
    @Mapping(target = "rowVersion",             constant = "1")
    public abstract IpUser toEntity(User user);

    @InheritConfiguration(name = "toEntity")
    public abstract void fillEntityFields(User source, @MappingTarget IpUser target);

    @AfterMapping
    protected void afterToEntityMapper(User user, @MappingTarget IpUser ipUser){
        //updating userGroup's userId!
        ipUser.getUserGroups().forEach(g -> g.getCfThisUserGroupPK().setUserId(user.getUserId()));
    }

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<User> toCoreList(List<IpUser> ipUsers);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<IpUser> toEntityList(List<User> users);

}
