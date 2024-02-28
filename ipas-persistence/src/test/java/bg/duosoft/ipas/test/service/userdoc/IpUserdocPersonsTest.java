package bg.duosoft.ipas.test.service.userdoc;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPersonPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.repository.entity.nomenclature.CfUserdocPersonRoleRepository;
import bg.duosoft.ipas.persistence.repository.entity.userdoc.IpUserdocRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.junit.Assert.*;

public class IpUserdocPersonsTest extends TestBase {
    @Autowired
    private CfUserdocPersonRoleRepository cfUserdocPersonRoleRepository;

    @Autowired
    private IpUserdocRepository ipUserdocRepository;

    @Test
    @Transactional
    public void selectUserdocPersonRoleApplicant() {
        CfUserdocPersonRole cfUserdocPersonRole = cfUserdocPersonRoleRepository.findById(UserdocPersonRole.APPLICANT).orElse(null);
        assertNotNull(cfUserdocPersonRole);
    }

    @Test
    @Transactional
    public void addUserdocPerson() {
        IpDocPK pk = new IpDocPK("BG", "E", 2004, 826716);
        IpUserdoc ipUserdoc = ipUserdocRepository.findById(pk).orElse(null);
        assertNotNull(ipUserdoc);

        IpUserdocPerson ipUserdocPerson = new IpUserdocPerson();
        ipUserdocPerson.setRowVersion(1);
        ipUserdocPerson.setPk(new IpUserdocPersonPK(pk.getDocOri(), pk.getDocLog(), pk.getDocSer(), pk.getDocNbr(), 106619, 1, UserdocPersonRole.APPLICANT));
        ipUserdoc.getPersons().add(ipUserdocPerson);

        IpUserdoc resultUserdoc = ipUserdocRepository.save(ipUserdoc);
        assertNotNull(resultUserdoc);
        assertFalse(CollectionUtils.isEmpty(resultUserdoc.getPersons()));
    }

    @Test
    @Transactional
    public void removeUserdocPerson() {
        addUserdocPerson();

        IpDocPK pk = new IpDocPK("BG", "E", 2004, 826716);
        IpUserdoc ipUserdoc = ipUserdocRepository.findById(pk).orElse(null);
        assertNotNull(ipUserdoc);
        assertFalse(CollectionUtils.isEmpty(ipUserdoc.getPersons()));

        int oldSize = ipUserdoc.getPersons().size();
        IpUserdocPerson remove = ipUserdoc.getPersons().remove(0);
        assertNotNull(remove);

        IpUserdoc resultUserdoc = ipUserdocRepository.save(ipUserdoc);
        assertNotNull(resultUserdoc);
        assertEquals(resultUserdoc.getPersons().size(), oldSize - 1);
    }

}
