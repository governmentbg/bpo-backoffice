package bg.duosoft.ipas.test.mapper.structure;

import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.structure.OfficeDivisionMapper;
import bg.duosoft.ipas.core.model.structure.OfficeDivision;
import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDivisionExtended;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeDivisionExtendedRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: ggeorgiev
 * Date: 24.6.2019 г.
 * Time: 17:08
 */
public class DivisionMapperTest extends TestBase {
    @Autowired
    private OfficeDivisionExtendedRepository officeDivisionExtendedRepository;
    @Autowired
    private OfficeDivisionMapper officeDivisionMapper;

    private CfOfficeDivisionExtended originalCfDivision;
    private OfficeDivision transformedDivision;
    private CfOfficeDivisionExtended transformedCfDivision;
    public void init(String divisionCode) {
        originalCfDivision = officeDivisionExtendedRepository.getOne(divisionCode);
        transformedDivision = officeDivisionMapper.toCore(originalCfDivision);
        transformedCfDivision = officeDivisionMapper.toEntity(transformedDivision);
    }
    @Test
    @Transactional
    public void transformIpDivisionToDivisionTestData() {
        init("999");
            Assert.assertEquals(originalCfDivision.getOfficeDivisionCode(), transformedDivision.getOfficeStructureId().getOfficeDivisionCode());
        Assert.assertEquals(originalCfDivision.getName(), transformedDivision.getName());
        Assert.assertEquals(originalCfDivision.getIndInactive(),  MapperHelper.getBooleanAsText(!transformedDivision.getActive()));
        if (originalCfDivision.getSignatureUser() != null) {
            assertEquals(originalCfDivision.getSignatureUser().getUserId(), transformedDivision.getSignatureUser().getUserId());
            assertEquals(originalCfDivision.getSignatureUser().getUserName(), transformedDivision.getSignatureUser().getUserName());
        } else {
            assertNull(transformedDivision.getSignatureUser());
        }
    }


    @Test
    @Transactional
    public void transformIpDivisionToDivisionRevertToIpDivisionTestData() {
        init("01");
        _assertEquals(originalCfDivision, transformedCfDivision, CfOfficeDivisionExtended::getOfficeDivisionCode);
        _assertEquals(originalCfDivision, transformedCfDivision, CfOfficeDivisionExtended::getName);
        _assertEquals(originalCfDivision, transformedCfDivision, CfOfficeDivisionExtended::getIndInactive);
        if (originalCfDivision.getSignatureUser() != null) {
            _assertEquals(originalCfDivision.getSignatureUser(), transformedCfDivision.getSignatureUser(), IpUser::getUserId);
            _assertEquals(originalCfDivision.getSignatureUser(), transformedCfDivision.getSignatureUser(), IpUser::getUserName);
        } else {
            assertNull(transformedCfDivision.getSignatureUser());
        }
    }

}
