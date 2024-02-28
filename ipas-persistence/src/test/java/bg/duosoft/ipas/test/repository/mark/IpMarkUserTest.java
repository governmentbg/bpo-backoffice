package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
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
import static org.junit.Assert.assertNotEquals;

public class IpMarkUserTest extends TestBase {

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private IpMark getMark() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2018, 152736);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        return ipMark;
    }
    @Transactional
    @Test
    public void updateUser() {
        IpMark ipMark = getMark();
        assertNotEquals((Object)1, ipMark.getCaptureUser().getUserId());
        IpUser user = new IpUser();
        user.setUserId(1);
        ipMark.setCaptureUser(user);
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals((Object)1, updated.getCaptureUser().getUserId());

    }

    @Transactional
    @Test
    public void deleteUser() {
        IpMark ipMark = getMark();
        ipMark.setCaptureUser(null);
        IpMark save = ipMarkRepository.saveAndFlush(ipMark);
        Assert.assertNull(save.getCaptureUser());
    }


}
