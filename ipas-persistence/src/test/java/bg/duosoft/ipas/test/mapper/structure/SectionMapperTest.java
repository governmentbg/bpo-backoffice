package bg.duosoft.ipas.test.mapper.structure;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.structure.OfficeSectionMapper;
import bg.duosoft.ipas.core.model.structure.OfficeSection;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSection;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionExtended;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeSectionPK;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeSectionExtendedRepository;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 24.6.2019 г.
 * Time: 17:08
 */
public class SectionMapperTest extends StructureTestBase {
    @Autowired
    private OfficeSectionExtendedRepository officeSectionExtendedRepository;
    @Autowired
    private OfficeSectionMapper officeSectionMapper;
    @PersistenceContext
    private EntityManager em;

    private CfOfficeSectionExtended originalCfSection;
    private OfficeSection transformedSection;
    private CfOfficeSection transformedCfSection;
    public void init(String divisionCode, String departmentCode, String sectionCode) {
        originalCfSection = officeSectionExtendedRepository.getOne(new CfOfficeSectionPK(divisionCode, departmentCode, sectionCode));
        transformedSection = officeSectionMapper.toCore(originalCfSection);
        transformedCfSection = officeSectionMapper.toEntity(transformedSection);
    }
    @Test
    @Transactional
    public void transformCfSectionToSection() {
        init(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION);
        Assert.assertEquals(originalCfSection.getCfOfficeSectionPK().getOfficeDivisionCode(), transformedSection.getOfficeStructureId().getOfficeDivisionCode());
        Assert.assertEquals(originalCfSection.getCfOfficeSectionPK().getOfficeDepartmentCode(), transformedSection.getOfficeStructureId().getOfficeDepartmentCode());
        Assert.assertEquals(originalCfSection.getCfOfficeSectionPK().getOfficeSectionCode(), transformedSection.getOfficeStructureId().getOfficeSectionCode());
        Assert.assertEquals(originalCfSection.getName(), transformedSection.getName());
        Assert.assertEquals(originalCfSection.getIndInactive(),  MapperHelper.getBooleanAsText(!transformedSection.getActive()));
        if (originalCfSection.getSignatureUser() != null) {
            assertEquals(originalCfSection.getSignatureUser().getUserId(), transformedSection.getSignatureUser().getUserId());
            assertEquals(originalCfSection.getSignatureUser().getUserName(), transformedSection.getSignatureUser().getUserName());
        } else {
            assertNull(originalCfSection.getSignatureUser());
        }
    }

//    @Test
//    @Transactional
//    public void testFillCoreEntityBeans() {
//        init(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION);
//        transformedSection.setName("alabala");
//        CfOfficeSection newEntity = officeSectionRepository.getOne(new CfOfficeSectionPK(MOCK_DIVISION, MOCK_ACTIVE_DEPARTMENT, MOCK_ACTIVE_SECTION));
//        em.detach(newEntity);
//        newEntity.setRowVersion(null);
//        newEntity.setXmlDesigner("test");
//        officeSectionMapper.fillEntityBean(transformedSection, newEntity);
//        newEntity.getCfOfficeSectionPK().setOfficeSectionCode("123456");
//        assertEquals((Object)1, newEntity.getRowVersion());
//        assertEquals("test", newEntity.getXmlDesigner());
//
//        transformedSection.setName(null);
//        officeSectionMapper.fillCoreBean(newEntity, transformedSection);
//        assertEquals("alabala", transformedSection.getName());
//        assertEquals("123456", transformedSection.getOfficeStructureId().getOfficeSectionCode());
//
//    }



//    @Test
//    @Transactional
//    public void transformIpDivisionToDivisionRevertToIpDivisionTestData() {
//        init("01");
//        _assertEquals(originalCfDivision, transformedCfDivision, CfOfficeDivision::getOfficeDivisionCode);
//        _assertEquals(originalCfDivision, transformedCfDivision, CfOfficeDivision::getName);
//        _assertEquals(originalCfDivision.getExtOfficeDivision(), transformedCfDivision.getExtOfficeDivision(), ExtOfficeDivision::getIndInactive);
//        if (originalCfDivision.getSignatureUser() != null) {
//            _assertEquals(originalCfDivision.getSignatureUser(), transformedCfDivision.getSignatureUser(), IpUser::getUserId);
//            _assertEquals(originalCfDivision.getSignatureUser(), transformedCfDivision.getSignatureUser(), IpUser::getUserName);
//        } else {
//            assertNull(transformedCfDivision.getSignatureUser());
//        }
//    }

}
