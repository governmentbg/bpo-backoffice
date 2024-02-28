package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClassesPK;
import bg.duosoft.ipas.persistence.repository.entity.mark.*;
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
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IpMarkNiceClassesTest extends TestBase {
    private Long NICE_CLASS_CODE = 18L;
    private String NICE_CLASS_STATUS = "R";
    private Long NICE_CLASS_EDITION = 0L;
    private String NICE_CLASS_VERSION = "0";

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public IpMark getIpMark() {
        IpFilePK ipFilePK = new IpFilePK("BG", "D", 2013, 91540);
        return ipMarkRepository.getOne(ipFilePK);
    }


    @Test
    @Transactional
    public void testUpdateNiceClass() {

        IpMark ipMark = getIpMark();
        ipMark.getIpMarkNiceClasses().get(0).setNiceClassDescription("alabala");
        ipMark.getIpMarkNiceClasses().get(0).setNiceClassDescrLang2("alabala in english");
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals("alabala", updated.getIpMarkNiceClasses().get(0).getNiceClassDescription());
        assertEquals("alabala in english", updated.getIpMarkNiceClasses().get(0).getNiceClassDescrLang2());

    }
    @Test
    @Transactional
    public void testInsertNiceClass() {
        IpMark ipMark = getIpMark();
        IpFilePK ipFilePK = ipMark.getPk();
        Assert.assertNotNull(ipMark);

        int niceClassesFirstSize = ipMark.getIpMarkNiceClasses().size();

        IpMarkNiceClasses classes = new IpMarkNiceClasses();
        IpMarkNiceClassesPK pk = new IpMarkNiceClassesPK(ipFilePK.getFileSeq(), ipFilePK.getFileTyp(), ipFilePK.getFileSer(), ipFilePK.getFileNbr(), NICE_CLASS_CODE, NICE_CLASS_STATUS);
        classes.setPk(pk);
        classes.setNiceClassEdition(NICE_CLASS_EDITION);
        classes.setRowVersion(1);
        classes.setNiceClassVersion(NICE_CLASS_VERSION);
        ipMark.getIpMarkNiceClasses().add(classes);

        IpMark save = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(save.getIpMarkNiceClasses().size(), (niceClassesFirstSize + 1));
    }

    @Test
    @Transactional
    public void testDeleteNiceClass() {
        IpMark ipMark = getIpMark();
        Assert.assertNotNull(ipMark);
        int oldSize = ipMark.getIpMarkNiceClasses().size();
        List<IpMarkNiceClasses> ipMarkNiceClasses = ipMark.getIpMarkNiceClasses();
        ipMarkNiceClasses.remove(ipMarkNiceClasses.size() - 1);
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(oldSize - 1, updated.getIpMarkNiceClasses().size());
    }

}
