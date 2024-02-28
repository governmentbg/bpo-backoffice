package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.mark.IpName;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpNameRepository;
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

/**
 * User: ggeorgiev
 * Date: 28.3.2019 г.
 * Time: 14:57
 */
public class IpNameRepositoryTest extends TestBase {
    @PersistenceContext
    private EntityManager em;
    public static final int MICROSOFT_MARK_CODE = 135923;

    @Autowired
    private IpNameRepository ipNameRepository;
    @Transactional
    @Test
    public void getExistingIpName() {
        IpName n = new IpName();
        n.setMarkName("MICROSOFT");
        n = ipNameRepository.getOrInsertIpName(n);
        assertEquals((Object) 135923, n.getMarkCode());
        assertEquals("MICROSOFT", n.getMarkName());
        Assert.assertNull(n.getMapDenomination());
        Assert.assertNull(n.getMarkNameLang2());
    }


    @Transactional
    @Test
    public void getNonExistingIpName() {
        IpName n = new IpName();
        String markName = "MICROSOFTttttttttadfadfasdadadfa";
        n.setMarkName(markName);
        n.setMarkNameLang2(markName + "1");
        n.setMapDenomination(markName + "2");
        n = ipNameRepository.getOrInsertIpName(n);
        assertEquals(ipNameRepository.getLastId(), n.getMarkCode());
        assertEquals(markName, n.getMarkName());
        assertEquals(markName + "1", n.getMarkNameLang2());
        assertEquals(markName + "2", n.getMapDenomination());
    }
}
