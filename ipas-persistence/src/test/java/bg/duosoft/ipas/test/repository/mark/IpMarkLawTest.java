package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLaw;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;

public class IpMarkLawTest extends TestBase {

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void updateMarkLawCode() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2018, 152736);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        CfLaw cfLaw = new CfLaw();
        cfLaw.setLawCode(ipMark.getCfLaw().getLawCode() == 1 ? 2 : 1);
        ipMark.setCfLaw(cfLaw);
        IpMark saved = ipMarkRepository.save(ipMark);
        entityManager.flush();
        assertEquals(2, saved.getCfLaw().getLawCode().intValue());
    }


    @Test
    @Transactional
    public void deleteMarkLawCode() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2018, 152736);
        IpMark ipMark = ipMarkRepository.getOne(ipFilePK);
        ipMark.setCfLaw(null);
        IpMark saved = ipMarkRepository.save(ipMark);
        entityManager.flush();
        Assert.assertNull(saved.getCfLaw());
    }


}
