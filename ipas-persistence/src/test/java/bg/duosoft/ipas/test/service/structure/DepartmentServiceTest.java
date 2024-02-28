package bg.duosoft.ipas.test.service.structure;

import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 13:46
 */
public class DepartmentServiceTest extends StructureTestBase {
    @Autowired
    private OfficeDepartmentService officeDepartmentService;
    @Autowired
    private SimpleUserService userService;
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Test
    public void testArchiveNotEmptyDepartment() {
        OfficeDepartment department = officeDepartmentService.getDepartment(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT);
        try {
            officeDepartmentService.archiveDepartment(department);
        } catch (IpasValidationException e) {
            assertEquals(2, e.getErrors().size());
        }
    }

    @Transactional
    @Test
    public void testArchiveEmptyDepartment() {
        OfficeDepartment department = officeDepartmentService.getDepartment(MOCK_DIVISION, MOCK_ACTIVE_EMPTY_DEPARTMENT);
        officeDepartmentService.archiveDepartment(department);
        em.flush();
        em.clear();
        department = officeDepartmentService.getDepartment(MOCK_DIVISION, MOCK_ACTIVE_EMPTY_DEPARTMENT);
        assertTrue(!department.getActive());
    }

    @Transactional
    @Test
    public void testTransferDepartment() {
        OfficeDepartment department = officeDepartmentService.getDepartment(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT);

        List<CUser> users = userService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, null, false, false);
        int initialUsersSize = users.size();
        OfficeDepartment newDepartment = officeDepartmentService.transferDepartments(Arrays.asList(department), "01").get(0);
        em.flush();
        users = userService.getUsers(newDepartment.getOfficeStructureId().getOfficeDivisionCode(), newDepartment.getOfficeStructureId().getOfficeDepartmentCode(), null, false, false);
        assertEquals(initialUsersSize, users.size());
        try {
            OfficeDepartment empty = officeDepartmentService.getDepartment(MOCK_DIVISION, MOCK_ACTIVE_SECTION);
            assertNull(empty);
        } catch (EntityNotFoundException e) {
            assertNotNull(e);
        }

    }
}
