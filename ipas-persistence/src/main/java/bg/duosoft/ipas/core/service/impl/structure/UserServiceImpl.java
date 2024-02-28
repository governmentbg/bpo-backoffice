package bg.duosoft.ipas.core.service.impl.structure;

import bg.duosoft.ipas.core.mapper.structure.CfThisUserMapper;
import bg.duosoft.ipas.core.mapper.structure.UserMapper;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.impl.ServiceBaseImpl;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.core.validation.config.IpasValidatorDefinition;
import bg.duosoft.ipas.core.validation.structure.SaveUserValidator;
import bg.duosoft.ipas.core.validation.structure.TransferUserValidator;
import bg.duosoft.ipas.integration.portal.model.core.CPortalUser;
import bg.duosoft.ipas.integration.portal.utils.PortalUserUtils;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisUser;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisUserGroup;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.user.CfThisUserRepository;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import bg.duosoft.ipas.persistence.repository.nonentity.UserRepository;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl extends ServiceBaseImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    protected IpUserRepository ipUserRepository;
    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CfThisUserMapper cfThisUserMapper;
    @Autowired
    private CfThisUserRepository cfThisUserRepository;

    @Override
    public List<Integer> getDepartmentAndAuthorizedByUserIds(Integer userId) {
        if (Objects.isNull(userId)){
            throw new RuntimeException("Empty user id");
        }
        Set<Integer> idsResultSet = new HashSet<>();
        List<Integer> idsResultList = new ArrayList<>();
        if (SecurityUtils.getLoggedUserId().equals(userId)){
            idsResultSet.addAll(SecurityUtils.getLoggedUserAndAuthorizedByUserIds());
            idsResultSet.addAll(userRepository.findDepartmentUsersByMainUser(userId));
        }
        else{
            idsResultSet.add(userId);
        }
        idsResultList.addAll(idsResultSet);
        return idsResultList;
    }

    public User getUser(int id) {
        IpUser ipUser = ipUserRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return toUser(ipUser);
    }

    public User getUserByLogin(String login) {
        IpUser ipUser = ipUserRepository.findByLogin(login);
        return toUser(ipUser);
    }


    @Override
    public List<String> getRolesPerUser(int userId) {
        return ipUserRepository.getSecurityRoles(userId).stream().map(r -> r.getRoleName()).collect(Collectors.toList());
    }

    @Override
    public List<User> selectUsersByLoginNames(List<String> loginNames) {
        if (CollectionUtils.isEmpty(loginNames))
            return null;

        List<IpUser> ipUsers = ipUserRepository.selectUsersByLoginNames(loginNames);
        if (CollectionUtils.isEmpty(ipUsers))
            return null;

        return toUserList(ipUsers);
    }

    @Override
    public User synchronizeUser(CPortalUser portalUser, User user) {
        if (Objects.isNull(user)) {
            user = new User();
        }
        PortalUserUtils.fillIpUserFields(portalUser, user);
        int id = saveUser(user);
        updateLastUpdateDate(new Date(), id);
        return getUser(id);
    }

    @Override
    public void updateLastUpdateDate(Date lastUpdateDate, int userId) {
        Integer loggedUserId = SecurityUtils.getLoggedUserId();
        ipUserRepository.updateLastUpdateDate(lastUpdateDate, loggedUserId, userId);
    }

    public List<User> getUsersByPartOfName(String partOfName) {
        return ipUserRepository.findByUsernameLike(partOfName, false).stream().map(this::toUser).collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = "simpleUser", key = "#user.userId", condition = "#user.userId != null")
    })
    @IpasValidatorDefinition(SaveUserValidator.class)
    public int saveUser(User user) {
        Integer userId = user.getUserId();
        if (userId == null) {//inserting user...
            userId = insertUser(user);
        } else {
            updateUser(user);
        }
        return userId;
    }

    public void updateUser(User user) {
        IpUser u = ipUserRepository.getOne(user.getUserId());
        userMapper.fillEntityFields(user, u);
        updateUserEntity(u);
    }

    public synchronized int insertUser(User user) {
        int userId = ipUserRepository.getNextUserId();
        IpUser ipUser = userMapper.toEntity(user);
        ipUser.setUserId(userId);

        //inserting record inside cf_this_user... It should happen before the IpUser insertion, because, the IpUser has relation to CfThisUserGroup, which has relations to CfThisUser...
        CfThisUser cfThisUser = cfThisUserMapper.ipUserToCfThisUser(ipUser);
        cfThisUserRepository.save(cfThisUser);

        List<CfThisUserGroup> userGroups = ipUser.getUserGroups();
        if (userGroups != null) {
            userGroups.forEach(ug -> ug.getCfThisUserGroupPK().setUserId(userId));
        }
        insertInsertableEntity(ipUser);

        return userId;
    }


    @IpasValidatorDefinition({TransferUserValidator.class})
    public void transferUsers(List<Integer> users, OfficeStructureId newStructureId) {
        int res = ipUserRepository.transferUsers(users, newStructureId.getOfficeDivisionCode(), newStructureId.getOfficeDepartmentCode(), newStructureId.getOfficeSectionCode());
        if (res != users.size()) {
            throw new RuntimeException(String.format("wrong records updated! Expected:%s Actual:%s", users.size(), res));
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "simpleUser", key = "{#user.userId}")
    })
    public void archiveUser(User user) {
        IpUser dbUser = ipUserRepository.getOne(user.getUserId());
        dbUser.setIndInactive("S");
        updateUserEntity(dbUser);
    }

    void updateUserEntity(IpUser user) {
        CfThisUser cfThisUser = cfThisUserMapper.ipUserToCfThisUser(user);
        cfThisUserRepository.save(cfThisUser);

        updateUpdatableEntity(user);
    }

    private User toUser(IpUser ipUser) {
        if (ipUser == null) {
            return null;
        }
        return userMapper.toCore(ipUser);
    }

    private List<User> toUserList(List<IpUser> list) {
        List<User> users = new ArrayList<>();
        for (IpUser ipUser : list) {
            users.add(toUser(ipUser));
        }
        return users.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

}
