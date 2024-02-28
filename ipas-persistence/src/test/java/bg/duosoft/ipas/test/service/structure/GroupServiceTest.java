package bg.duosoft.ipas.test.service.structure;

import bg.duosoft.ipas.core.model.structure.Group;
import bg.duosoft.ipas.core.service.structure.GroupService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 25.7.2019 г.
 * Time: 17:44
 */
public class GroupServiceTest extends TestBase {
    @Autowired
    private GroupService groupService;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    public void insertGroup() {
        Group g = new Group();
        g.setGroupName("TestGroup");
        g.setDescription("TestDescription");
        g.setRoleNames(new ArrayList<>());
        g.getRoleNames().add("ipas.home.page");
        int savedGroupId = groupService.saveGroup(g);
        em.flush();
        Group savedGroup = groupService.getGroup(savedGroupId);
        _assertEquals(g, savedGroup, Group::getGroupName);
        _assertEquals(g, savedGroup, Group::getDescription);
        for (int i = 0; i < g.getRoleNames().size(); i++) {
            String originalRole = g.getRoleNames().get(i);
            String savedRole = savedGroup.getRoleNames().get(i);
            assertEquals(originalRole, savedRole);
        }
    }

    @Test
    @Transactional
    public void testAddRoleToGroup() {
        em.createNativeQuery("INSERT INTO EXT_USER.CF_SECURITY_ROLES (ROLE_NAME, DESCRIPTION) VALUES ('test', 'test')").executeUpdate();
        Group group = groupService.getGroup(1);
        int size = group.getRoleNames().size();
        group.getRoleNames().add("test");

        int savedGroupId = groupService.saveGroup(group);
        em.flush();
        Group savedGroup = groupService.getGroup(savedGroupId);
        assertEquals(size + 1, savedGroup.getRoleNames().size());
    }

    @Test
    @Transactional
    public void testRemoveRoleFromGroup() {
        Group group = groupService.getGroup(1);
        int size = group.getRoleNames().size();

        group.getRoleNames().remove(0);

        int savedGroupId = groupService.saveGroup(group);
        em.flush();
        Group savedGroup = groupService.getGroup(savedGroupId);
        assertEquals(size - 1, savedGroup.getRoleNames().size());
    }
}
