package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkReprs;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkReprsPK;
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

import java.util.List;

import static org.junit.Assert.assertEquals;

public class IpMarkRepresentativesTests extends TestBase {


    @Autowired
    private IpMarkRepository ipMarkRepository;

    private IpMark getMark() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2004, 72946);//5 representatives
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        return ipMark;
    }

    @Test
    @Transactional
    public void removeMarkRepresentative() {
        IpMark ipMark = getMark();
        List<IpMarkReprs> ipMarkRepresentatives = ipMark.getRepresentatives();
        int originalSize = ipMarkRepresentatives.size();
        IpMarkReprs toBeDeleted = ipMarkRepresentatives.stream().filter(ipMarkReprs -> ipMarkReprs.getPk().getPersonNbr() == 116742).findFirst().orElse(null);

        boolean remove = ipMarkRepresentatives.remove(toBeDeleted);
        Assert.assertTrue(remove);
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(originalSize - 1, updated.getRepresentatives().size());
    }

    @Test
    @Transactional
    public void addMarkRepresentative() {
        IpMark ipMark = getMark();
        IpFilePK markPk = ipMark.getPk();
        List<IpMarkReprs> ipMarkRepresentatives = ipMark.getRepresentatives();
        int originalSize = ipMarkRepresentatives.size();
        Assert.assertNotNull(ipMarkRepresentatives);

        IpMarkReprsPK pk = new IpMarkReprsPK(markPk.getFileSeq(), markPk.getFileTyp(), markPk.getFileSer(), markPk.getFileNbr(), 370219, 1, "AG");

        IpMarkReprs newRepr = new IpMarkReprs();
        newRepr.setPk(pk);
        newRepr.setRowVersion(1);

        ipMarkRepresentatives.add(newRepr);

        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(originalSize + 1, updated.getRepresentatives().size());


    }


}
