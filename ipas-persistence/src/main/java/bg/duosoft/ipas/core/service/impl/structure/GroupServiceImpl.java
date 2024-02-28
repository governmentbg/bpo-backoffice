package bg.duosoft.ipas.core.service.impl.structure;

import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.mapper.structure.GroupMapper;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.Group;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.impl.ServiceBaseImpl;
import bg.duosoft.ipas.core.service.structure.GroupService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.structure.AddUserToGroupValidator;
import bg.duosoft.ipas.core.validation.structure.DeleteUserFromGroupValidator;
import bg.duosoft.ipas.core.validation.structure.SaveGroupValidator;
import bg.duosoft.ipas.persistence.model.entity.user.CfSecurityRoles;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisGroup;
import bg.duosoft.ipas.persistence.repository.entity.user.CfSecurityRolesRepository;
import bg.duosoft.ipas.persistence.repository.entity.user.CfThisGroupRepository;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 24.6.2019 г.
 * Time: 16:28
 */
@Transactional
@Service
public class GroupServiceImpl extends ServiceBaseImpl implements GroupService {
    @Autowired
    private CfThisGroupRepository cfThisGroupRepository;

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private CfSecurityRolesRepository rolesRepository;
    @Autowired
    private IpUserRepository ipUserRepository;
    @Autowired
    private SimpleUserMapper simpleUserMapper;
    @Autowired
    private UserService userService;


    @Cacheable("userGroupsMap")
    public Map<Integer, String> getGroupsMap() {
        List<CfThisGroup> allGroups = cfThisGroupRepository.findAll();
        return allGroups.stream().collect(Collectors.toMap(CfThisGroup::getGroupId, CfThisGroup::getGroupname, (r1, r2) -> {
            throw new RuntimeException();
        }, TreeMap::new));
    }

    @Override
    public Group getGroup(int groupId) {
        CfThisGroup entity = cfThisGroupRepository.getOne(groupId);
        return groupMapper.toCore(entity);
    }

    @Override
    @IpasValidatorDefinition({SaveGroupValidator.class})
    @CacheEvict("userGroupsMap")
    public int saveGroup(Group group) {
        Integer res = group.getGroupId();
        if (group.getGroupId() == null) {
            res = insertGroup(group);
        } else {
            updateGroup(group);
//            updateUsersPerGroup(res, userIds);
        }
        return res;
    }


    /*private void updateUsersPerGroup(int groupId, List<Integer> userIds) {
        //delete the users, that are removed from the group
        Query deleteQuery = em.createQuery("DELETE from CfThisUserGroup ug where ug.cfThisUserGroupPK.userId not in :userIds and ug.cfThisUserGroupPK.groupId = :groupId");
        deleteQuery.setParameter("userIds", userIds);
        deleteQuery.setParameter("groupId", groupId);
        deleteQuery.executeUpdate();

        if (!Collections.isEmpty(userIds)) {
            //users that do not exist in the database:
            List<Integer> usersByGroup = ipUserRepository.getUsersByGroup(groupId).stream().map(r -> r.getUserId()).collect(Collectors.toList());
            userIds.removeAll(usersByGroup);
            for (Integer userId : userIds) {
                CfThisUserGroup e = userGroupToGroupIdMapper.toEntity(groupId);
                e.getCfThisUserGroupPK().setUserId(userId);
                em.persist(e);
            }
        }
    }*/

    @Override
    @Cacheable("rolesMap")
    public Map<String, String> getRolesMap() {
        return rolesRepository.findAll().stream().collect(Collectors.toMap(CfSecurityRoles::getRoleName, CfSecurityRoles::getDescription));
    }

    public void updateGroup(Group group) {
        CfThisGroup entity = cfThisGroupRepository.getOne(group.getGroupId());
        groupMapper.fillEntityBean(group, entity);
        updateUpdatableEntity(entity);
    }

    public synchronized int insertGroup(Group group) {
        CfThisGroup entity = groupMapper.toEntity(group);
        int groupId = cfThisGroupRepository.getNextGroupId();
        entity.setGroupId(groupId);
        if (entity.getGroupSecurityRoles() != null) {
            entity.getGroupSecurityRoles().forEach(r -> r.getCfGroupSecurityRolePK().setGroupId(groupId));
        }
        insertInsertableEntity(entity);
        return groupId;
    }

    @Override
    public List<CUser> getUsersByGroup(int groupId) {
        return simpleUserMapper.toCoreList(ipUserRepository.getUsersByGroup(groupId));
    }

    @Override
    @IpasValidatorDefinition({DeleteUserFromGroupValidator.class})
    public void removeUserFromGroup(Integer groupId, Integer userId) {
        User user = userService.getUser(userId);
        boolean removed = user.getGroupIds().remove(groupId);
        if (removed) {
            userService.saveUser(user);
        }
    }

    @Override
    @IpasValidatorDefinition({AddUserToGroupValidator.class})
    public void addUserToGroup(Integer groupId, Integer userId) {
        User user = userService.getUser(userId);
        user.getGroupIds().add(groupId);
        userService.saveUser(user);
    }
}
