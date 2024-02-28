package bg.duosoft.ipas.test.service.structure;

import bg.duosoft.ipas.core.mapper.structure.OfficeSectionPkMapper;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.core.model.structure.OfficeStructureId;
import bg.duosoft.ipas.core.service.structure.OfficeSectionService;
import bg.duosoft.ipas.core.service.person.SimpleUserService;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSection;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeSectionExtendedRepository;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 5.7.2019 г.
 * Time: 13:46
 */
public class SectionServiceTest extends StructureTestBase {
    @Autowired
    private OfficeSectionService officeSectionService;
    @Autowired
    private OfficeSectionPkMapper officeSectionPkMapper;
    @Autowired
    private SimpleUserService userService;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private OfficeSectionExtendedRepository officeSectionExtendedRepository;

    @Transactional
    @Test
    public void testTransferSection() {
        OfficeSection section = officeSectionService.getSection(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION);
        List<CUser> users = userService.getUsers(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION, false, false);
        int initialUsersSize = users.size();
        OfficeSection newSection = officeSectionService.transferSections(Arrays.asList(section), MOCK_DIVISION, MOCK_INACTIVE_DEPARTMENT).get(0);
        em.flush();
        users = userService.getUsers(newSection.getOfficeStructureId().getOfficeDivisionCode(), newSection.getOfficeStructureId().getOfficeDepartmentCode(), newSection.getOfficeStructureId().getOfficeSectionCode(), false, false);
        assertEquals(initialUsersSize, users.size());
        OfficeSection empty = officeSectionService.getSection(MOCK_DIVISION, MOCK_ACTIVE_SECTION, MOCK_ACTIVE_SECTION);
        assertNull(empty);
    }

    @Test
    @Transactional
    public void updateSectionTestDateUpdated() {
        OfficeSection section = officeSectionService.getSection(MOCK_DIVISION, MOCK_INACTIVE_DEPARTMENT, MOCK_INACTIVE_SECTION);
        officeSectionService.updateSection(section);
        em.flush();
        CfOfficeSection entity = officeSectionExtendedRepository.getOne(officeSectionPkMapper.toEntity(section.getOfficeStructureId()));
        assertNotNull(entity.getLastUpdateDate());
        assertNotNull(entity.getLastUpdateUserId());
        assertNotNull(entity.getCreationUserId());
        assertNotNull(entity.getCreationDate());
    }

    @Test
    @Transactional
    public void insertSectionTestDateCreated() {
        OfficeSection section = new OfficeSection();
        section.setName("TestSection");
        section.setActive(true);
        section.setSignatureUser(new CUser("test", 907, false));
        section.setOfficeStructureId(new OfficeStructureId());
        section.getOfficeStructureId().setOfficeDivisionCode(MOCK_DIVISION);
        section.getOfficeStructureId().setOfficeDepartmentCode(MOCK_ACTIVE_EMPTY_DEPARTMENT);

        OfficeStructureId insertedSectionId = officeSectionService.insertSection(section);
        em.flush();

        CfOfficeSection insertedSectionEntity = officeSectionExtendedRepository.getOne(officeSectionPkMapper.toEntity(insertedSectionId));

        assertNotNull(insertedSectionEntity.getCreationDate());
        assertNotNull(insertedSectionEntity.getCreationUserId());
    }

}
