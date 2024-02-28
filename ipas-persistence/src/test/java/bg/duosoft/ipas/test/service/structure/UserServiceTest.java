package bg.duosoft.ipas.test.service.structure;

import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 25.6.2019 г.
 * Time: 11:04
 */
public class UserServiceTest extends StructureTestBase {
    @Autowired
    private UserService userService;
    @PersistenceContext
    private EntityManager em;


    @Test
    @Transactional
//    @Commit
    public void addGroupToUser() {
        User user = userService.getUser(4145);
        int groupsSize = user.getGroupIds().size();
        user.getGroupIds().add(3);
        userService.saveUser(user);
        em.flush();
        user = userService.getUser(4145);
        assertEquals(groupsSize + 1, user.getGroupIds().size());

        System.out.println(user.getGroupIds());
    }

    @Test
    @Transactional
//    @Commit
    public void testInsertUser() {
        User user = new User();

        user.setOfficeDivisionCode(MOCK_DIVISION);
        user.setOfficeDepartmentCode(MOCK_ACTIVE_DEPARTMENT);
        user.setOfficeSectionCode(MOCK_ACTIVE_SECTION);

        user.setEmail("test@bpo.bg");
        user.setFullName("Test User");
//        user.setGroupIds(Arrays.asList(1,3));//Vajno - poradi nqkakva prichina pri testovete ako se dobavi grupa kym user-a ima problemi s inserta, vypreki che nqma nikakva logika... Pyrvo se insertva user s id = xxx, sled tova se insertva zapis v cf_this_user s toq user_id i nakraq pri insert-a na cf_this_usergroups sys sy6toto user_id, gyrmi...
        user.setInitials("TU");
        user.setUserName("Test Testov");
        user.setTelephone("0000");
        user.setIndAdministrator(true);
        user.setIndExaminer(false);
        user.setIndInactive(false);
        user.setIndExternal(false);
        user.setLogin("testuser100");
        int insertedUserId = userService.saveUser(user);
        em.flush();
        User insertedUser = userService.getUser(insertedUserId);
        _assertEquals(user, insertedUser, User::getOfficeDivisionCode);
        _assertEquals(user, insertedUser, User::getOfficeDepartmentCode);
        _assertEquals(user, insertedUser, User::getOfficeSectionCode);
        _assertEquals(user, insertedUser, User::getEmail);
        _assertEquals(user, insertedUser, User::getFullName);
        _assertEquals(user, insertedUser, User::getInitials);
        _assertEquals(user, insertedUser, User::getUserName);
        _assertEquals(user, insertedUser, User::getTelephone);
        _assertEquals(user, insertedUser, User::getIndAdministrator);
        _assertEquals(user, insertedUser, User::getIndExaminer);
        _assertEquals(user, insertedUser, User::getIndInactive);
        _assertEquals(user, insertedUser, User::getIndExternal);
        _assertEquals(user, insertedUser, User::getLogin);
//        _assertEquals(user, insertedUser, User::getGroupIds);
    }
}
