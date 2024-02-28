package bg.duosoft.ipas.test.repository.user;

import bg.duosoft.ipas.persistence.model.entity.structure.CfOfficeDivisionExtended;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.repository.entity.structure.OfficeDivisionExtendedRepository;
import bg.duosoft.ipas.test.StructureTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 29.10.2019 г.
 * Time: 18:50
 */
public class OfficeDivisionTest extends StructureTestBase {
    @Autowired
    private OfficeDivisionExtendedRepository officeDivisionExtendedRepository;
    @PersistenceContext
    private EntityManager em;
    @Test
    @Transactional
    public void testInsertDivision() {
        CfOfficeDivisionExtended division = new CfOfficeDivisionExtended();
        division.setName("Testovo vedomstvo");
        String divisionCode = "666";
        division.setOfficeDivisionCode(divisionCode);
        division.setSignatureUser(new IpUser(4));
        division.setRowVersion(1);
        division.setCreationDate(new Date());
        division.setCreationUserId(4);
        division.setLastUpdateDate(new Date());
        division.setXmlDesigner("test");
        division.setIndInactive("S");
        em.persist(division);
        em.flush();

        CfOfficeDivisionExtended inserted = officeDivisionExtendedRepository.getOne(divisionCode);

        _assertEquals(division, inserted, CfOfficeDivisionExtended::getIndInactive);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getSignatureUser);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getOfficeDivisionCode);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getName);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getCreationDate);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getCreationUserId);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getLastUpdateDate);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getLastUpdateUserId);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getRowVersion);
        _assertEquals(division, inserted, CfOfficeDivisionExtended::getXmlDesigner);
    }
}
