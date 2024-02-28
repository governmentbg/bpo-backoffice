package bg.duosoft.ipas.test.repository.user;


import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisUserGroup;
import bg.duosoft.ipas.persistence.model.entity.user.CfThisUserGroupPK;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.user.IpUserRepository;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 21.6.2019 г.
 * Time: 16:53
 */
public class UserRepositoryTest extends StructureTestBase {
    private static int IPASPROD_USER_ID = 4;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private UserService userService;
    @Autowired
    private IpUserRepository ipUserRepository;

    @Test
    @Transactional
    public void countSameStructure() {
        long cnt = ipUserRepository.countUsersSameStructure(Arrays.asList(4146, 4152), MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, null);
        assertEquals(2, cnt);
    }

    @Test
    @Transactional
    public void countSameStructure2() {
        long cnt = ipUserRepository.countUsersSameStructure(Arrays.asList(4146, 4152), MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION);
        assertEquals(0, cnt);
    }


//    @Test
//    @Transactional
//    @Commit
//    public void test2() {
//        IpUser u = ipUserRepository.getOne(IPASPROD_USER_ID);
//        System.out.println("Groups ---> " + u.getUserGroups().get(0).getCfThisUserGroupPK().getGroupId());
//        CfThisUserGroup ug = new CfThisUserGroup();
//        ug.setUser(u);

//        CfThisGroup group = new CfThisGroup();
//        group.setGroupId(3);
//        ug.setGroup(group);
//        ug.setGroup();
//        ug.setCfThisUserGroupPK(new CfThisUserGroupPK());
//        ug.getCfThisUserGroupPK().setGroupId(3);
//        ug.getCfThisUserGroupPK().setUserId(u.getUserId());
//        em.persist(ug);
//        ug.setRowVersion(1);
//        u.getUserGroups().add(ug);
//        em.persist(u);
//        em.flush();
//        ipUserRepository.save(u);
//    }
}
