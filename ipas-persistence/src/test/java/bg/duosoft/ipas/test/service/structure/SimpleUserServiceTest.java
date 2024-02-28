package bg.duosoft.ipas.test.service.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: ggeorgiev
 * Date: 30.7.2019 г.
 * Time: 14:59
 */
public class SimpleUserServiceTest extends StructureTestBase {

    @Autowired
    private SimpleUserService simpleUserService;
    @Autowired
    private UserService userService;


    @Test
    @Transactional
    public void testGetOnlyActiveUsersByOnlyInDivision() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, null, null, true, true);
        assertEquals(1, users.size());
        Optional<CUser> inactiveUsers = users.stream().filter(u -> u.getIndInactive() == true).findAny();
        Optional<User> notInDivisionUsers = users.stream().map(u -> userService.getUser(u.getUserId())).filter(u -> !StringUtils.isEmpty(u.getOfficeDepartmentCode()) || !StringUtils.isEmpty(u.getOfficeSectionCode())).findAny();
        assertTrue(inactiveUsers.isEmpty());
        assertTrue(notInDivisionUsers.isEmpty());
    }


    @Test
    @Transactional
    public void testGetAllUsersByOnlyInDivision() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, null, null, true, false);
        assertEquals(2, users.size());
        Optional<CUser> inactiveUsers = users.stream().filter(u -> u.getIndInactive() == true).findAny();
        Optional<User> notInDivisionUsers = users.stream().map(u -> userService.getUser(u.getUserId())).filter(u -> !StringUtils.isEmpty(u.getOfficeDepartmentCode()) || !StringUtils.isEmpty(u.getOfficeSectionCode())).findAny();
        assertTrue(inactiveUsers.isPresent());
        assertTrue(notInDivisionUsers.isEmpty());
    }

    @Test
    @Transactional
    public void testGetAllUsersInDivisionIncludingDepartmentsSections() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, null, null, false, false);
        assertEquals(9, users.size());
        Optional<CUser> inactiveUsers = users.stream().filter(u -> u.getIndInactive() == true).findAny();
        Optional<User> notInDivisionUsers = users.stream().map(u -> userService.getUser(u.getUserId())).filter(u -> !StringUtils.isEmpty(u.getOfficeDepartmentCode()) || !StringUtils.isEmpty(u.getOfficeSectionCode())).findAny();
        assertTrue(inactiveUsers.isPresent());
        assertTrue(notInDivisionUsers.isPresent());
    }


    @Test
    @Transactional
    public void testGetOnlyActiveUsersInDivisionIncludingDepartmentsSections() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, null, null, false, true);
        assertEquals(4, users.size());
        Optional<CUser> inactiveUsers = users.stream().filter(u -> u.getIndInactive() == true).findAny();
        Optional<User> notInDivisionUsers = users.stream().map(u -> userService.getUser(u.getUserId())).filter(u -> !StringUtils.isEmpty(u.getOfficeDepartmentCode()) || !StringUtils.isEmpty(u.getOfficeSectionCode())).findAny();
        assertTrue(inactiveUsers.isEmpty());
        assertTrue(notInDivisionUsers.isPresent());
    }


    @Test
    @Transactional
    public void testGetOnlyActiveUsersInDepartmentIncludingSections() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, null, false, true);
        assertEquals(3, users.size());
    }

    @Test
    @Transactional
    public void testGeAllUsersInDepartmentIncludingSections() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, null, false, false);
        assertEquals(5, users.size());
    }

    @Test
    @Transactional
    public void testGetOnlyActiveUsersInDepartmentExcludingSections() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, null, true, true);
        assertEquals(1, users.size());
    }

    @Test
    @Transactional
    public void testGeAllUsersInDepartmentExcludingSections() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, null, true, false);
        assertEquals(2, users.size());
    }

    @Test
    @Transactional
    public void testGetOnlyActiveUsersInSection() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION, true, true);
        assertEquals(2, users.size());
    }

    @Test
    @Transactional
    public void testGetAllUsersInSection() {
        List<CUser> users = simpleUserService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION, true, false);
        assertEquals(3, users.size());
    }

    @Test
    @Transactional
    public void getUsersByPartOfName() {
        List<CUser> users = simpleUserService.findByUsernameLike("ipas", false);
        assertTrue(users.size() > 0);
    }
}
