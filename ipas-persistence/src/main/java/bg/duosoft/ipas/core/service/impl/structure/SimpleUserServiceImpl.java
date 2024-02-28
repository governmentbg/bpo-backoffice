package bg.duosoft.ipas.core.service.impl.structure;

import bg.duosoft.ipas.core.mapper.miscellaneous.SimpleUserMapper;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * User: ggeorgiev
 * Date: 26.7.2019 г.
 * Time: 16:54
 */
@Service
@Transactional
public class SimpleUserServiceImpl implements SimpleUserService {
    @Autowired
    private SimpleUserMapper simpleUserMapper;
    @Autowired
    private IpUserRepository ipUserRepository;

    @Override
    public List<CUser> findByUsernameLike(String username, boolean onlyActive) {
        if (StringUtils.isEmpty(username))
            return null;

        List<IpUser> users = ipUserRepository.findByUsernameLike(username, onlyActive);
        if (CollectionUtils.isEmpty(users))
            return null;

        List<CUser> cUsers = simpleUserMapper.toCoreList(users);
        sortCUsers(cUsers);
        return cUsers;
    }

    @Override
    @Cacheable(value = "simpleUser", key = "#id")
    public CUser findSimpleUserById(Integer id) {
        if (Objects.isNull(id))
            return null;

        IpUser ipUser = ipUserRepository.findById(id).orElse(null);
        if (Objects.isNull(ipUser))
            return null;

        return simpleUserMapper.toCore(ipUser);
    }

    @Override
    public List<CUser> getUsers(String divisionCode, String departmentCode, String sectionCode, boolean onlyInStructure, boolean onlyActive) {
        List<IpUser> users = ipUserRepository.getUsers(divisionCode, departmentCode, sectionCode, onlyInStructure, onlyActive);
        List<CUser> cUsers = simpleUserMapper.toCoreList(users);
        sortCUsers(cUsers);
        return cUsers;
    }


    private void sortCUsers(List<CUser> users){
        if (!CollectionUtils.isEmpty(users)){
            users.sort(Comparator.comparing(CUser::getUserName));
        }
    }
    /*@Override
    @Cacheable(value = "simpleUser", key = "{'all', #onlyActive}")
    public List<CUser> getAllSimpleUsers(boolean onlyActive) {
        return simpleUserMapper.toCoreList(ipUserRepository.findByOnlyActive(onlyActive));
    }*/
}
