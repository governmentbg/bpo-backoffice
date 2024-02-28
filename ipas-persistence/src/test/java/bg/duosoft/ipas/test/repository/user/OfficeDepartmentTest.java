package bg.duosoft.ipas.test.repository.user;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentExtended;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDepartmentPK;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeDepartmentExtendedRepository;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 1.7.2019 г.
 * Time: 16:10
 */
public class OfficeDepartmentTest extends StructureTestBase {

    @Autowired
    private OfficeDepartmentExtendedRepository officeDepartmentExtendedRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    public void testFindOfficeDepartmentsByName() {
        List<CfOfficeDepartmentExtended> res = officeDepartmentExtendedRepository.findByOfficeDepartmentNameContainingIgnoreCase("тест", false);
        assertEquals(1, res.size());
    }


    @Test
    @Transactional
    public void testFindAllOfficeDepartments() {
        List<CfOfficeDepartmentExtended> allDepartments = officeDepartmentExtendedRepository.findByOfficeDepartmentNameContainingIgnoreCase(null, false);
        List<CfOfficeDepartmentExtended> allActiveDepartments = officeDepartmentExtendedRepository.findByOfficeDepartmentNameContainingIgnoreCase(null, true);

        assertTrue("All offices should be greater than or equal to the only active offices", allDepartments.size() >= allActiveDepartments.size() );
    }


    @Test
    @Transactional
    public void testFindAllActiveDepartmentsOfDivision() {
        List<CfOfficeDepartmentExtended> activeDepartments = officeDepartmentExtendedRepository.getByOfficeDivisionCodeAndOnlyActive(MOCK_DIVISION, true);
        Optional<CfOfficeDepartmentExtended> inactiveDepartments = activeDepartments.stream().filter(od -> od.getIndInactive().equals("S")).findAny();
        assertTrue("There should be no active departments selected", inactiveDepartments.isEmpty());
        assertEquals(2, activeDepartments.size());
    }

    @Test
    @Transactional
    public void testFindAllDepartmentsOfDivision() {
        List<CfOfficeDepartmentExtended> activeDepartments = officeDepartmentExtendedRepository.getByOfficeDivisionCodeAndOnlyActive(MOCK_DIVISION, false);
        Map<String, List<CfOfficeDepartmentExtended>> groupedByIndInactive = activeDepartments.stream().collect(Collectors.groupingBy(r -> r.getIndInactive()));
        assertNotNull("There should be at least one inactive department", groupedByIndInactive.get("S"));
        assertNotNull("There should be at least one active department", groupedByIndInactive.get("N"));
        assertEquals(1, groupedByIndInactive.get("S").size());
        assertEquals(2, groupedByIndInactive.get("N").size());
//        assertEquals("Inactive Department", groupedByIndInactive.get("S").get(0).getName());
//        assertEquals("Active Department", groupedByIndInactive.get("N").get(0).getName());
    }


    @Test
    @Transactional
//    @Commit
    public void testInsertDepartment() {
        CfOfficeDepartmentExtended department = new CfOfficeDepartmentExtended();
        department.setName("Department 10000");
        department.setRowVersion(1);
        department.setIndInactive("S");
        department.setCfOfficeDepartmentPK(new CfOfficeDepartmentPK());
        department.getCfOfficeDepartmentPK().setOfficeDivisionCode(MOCK_DIVISION);
        String departmentCode = officeDepartmentExtendedRepository.getNextDepartmentCode(MOCK_DIVISION);
        department.getCfOfficeDepartmentPK().setOfficeDepartmentCode(departmentCode);
        em.persist(department);
        em.flush();
        CfOfficeDepartmentExtended inserted = officeDepartmentExtendedRepository.getOne(new CfOfficeDepartmentPK(MOCK_DIVISION, departmentCode));
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getName);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getCreationDate);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getRowVersion);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getXmlDesigner);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getLastUpdateUserId);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getLastUpdateDate);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getSignatureUser);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getCreationUserId);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getDivision);
        _assertEquals(department, inserted, CfOfficeDepartmentExtended::getIndInactive);
    }

}
