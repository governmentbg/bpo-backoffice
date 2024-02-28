package bg.duosoft.ipas.test.repository.user;

import bg.duosoft.ipas.persistence.model.entity.structure.*;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeSectionExtendedRepository;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 12:10
 */
public class OfficeSectionTest extends StructureTestBase {

    @Autowired
    private OfficeSectionExtendedRepository officeSectionExtendedRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    public void getOnlyActiveSections() {
        List<CfOfficeSectionExtended> activeSections = officeSectionExtendedRepository.getByOfficeDivisionCodeAndOfficeDepartmentCode(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, true);
        assertEquals(1, activeSections.size());
        assertEquals("Active Section 001", activeSections.get(0).getName());

        activeSections = officeSectionExtendedRepository.getByOfficeDivisionCodeAndOfficeDepartmentCode(MOCK_DIVISION, MOCK_INACTIVE_DEPARTMENT, true);
        assertEquals(0, activeSections.size());
    }


    @Test
    @Transactional
    public void getAllSections() {
        List<CfOfficeSectionExtended> activeSections = officeSectionExtendedRepository.getByOfficeDivisionCodeAndOfficeDepartmentCode(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, false);
        assertEquals(1, activeSections.size());
        assertEquals("Active Section 001", activeSections.get(0).getName());

        activeSections = officeSectionExtendedRepository.getByOfficeDivisionCodeAndOfficeDepartmentCode(MOCK_DIVISION, MOCK_INACTIVE_DEPARTMENT, false);
        assertEquals(1, activeSections.size());
        assertEquals("Inactive Section 002", activeSections.get(0).getName());
    }


    @Test
    @Transactional
//    @Commit
    public void testInsertSection() {
        CfOfficeSectionExtended section = new CfOfficeSectionExtended();
        section.setName("Секция 10000");
        section.setCreationDate(new Date());
        section.setCreationUserId(4);
        section.setIndDeliveryAnyMember("S");
        section.setIndDeliveryIgnore("N");
        section.setLastUpdateDate(new Date());
        section.setLastUpdateUserId(4);
        section.setRowVersion(1);
        section.setSignatureUser(new IpUser(4));
        section.setXmlDesigner("TEST");
        section.setIndInactive("N");

        section.setCfOfficeSectionPK(new CfOfficeSectionPK());
        section.getCfOfficeSectionPK().setOfficeDivisionCode(MOCK_DIVISION);
        section.getCfOfficeSectionPK().setOfficeDepartmentCode(MOCK_ACTIVE_DEPARTMENT);
        String sectionCode = officeSectionExtendedRepository.getNextSectionCode(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT);
        section.getCfOfficeSectionPK().setOfficeSectionCode(sectionCode);


        em.persist(section);
        em.flush();
        CfOfficeSectionExtended inserted = officeSectionExtendedRepository.getOne(new CfOfficeSectionPK(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, sectionCode));
        _assertEquals(section, inserted, CfOfficeSectionExtended::getIndInactive);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getCreationDate);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getCreationUserId);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getCfOfficeSectionPK);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getLastUpdateDate);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getLastUpdateUserId);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getIndDeliveryAnyMember);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getIndDeliveryIgnore);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getName);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getRowVersion);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getSignatureUser);
        _assertEquals(section, inserted, CfOfficeSectionExtended::getXmlDesigner);
    }

}
