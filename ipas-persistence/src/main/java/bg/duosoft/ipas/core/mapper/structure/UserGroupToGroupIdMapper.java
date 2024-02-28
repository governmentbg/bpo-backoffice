package bg.duosoft.ipas.core.mapper.structure;

import bg.duosoft.ipas.persistence.model.entity.user.CfThisUserGroup;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisUserGroupPK;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/**
 * User: ggeorgiev
 * Date: 21.6.2019 г.
 * Time: 16:31
 */
@Mapper(componentModel = "spring")
public abstract class UserGroupToGroupIdMapper {
    public Integer toCore(CfThisUserGroup cfThisGroup) {
        return cfThisGroup.getCfThisUserGroupPK().getGroupId();
    }


    public CfThisUserGroup toEntity(Integer group) {
        CfThisUserGroup ug = new CfThisUserGroup();
        ug.setRowVersion(1);
        ug.setCfThisUserGroupPK(new CfThisUserGroupPK());
        ug.getCfThisUserGroupPK().setGroupId(group);
        return ug;
    }

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<Integer> toCoreList(List<CfThisUserGroup> cfThisGroups);

    @IterableMapping(nullValueMappingStrategy = RETURN_DEFAULT)
    public abstract List<CfThisUserGroup> toEntityList(List<Integer> groups);
}
