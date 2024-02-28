package bg.duosoft.ipas.test.repository.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkPriorities;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkPrioritiesPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IpMarkPrioritiesTest extends TestBase {
    private String PRIORITY_APP_ID = "TEST";
    private String PRIORITY_COUNTRY = "BG";

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private IpMark getMark() {
        IpFilePK ipFilePK = new IpFilePK("BG", "N", 2017, 146030);
        IpMark ipMark = ipMarkRepository.findById(ipFilePK).orElse(null);
        return ipMark;
    }

    @Test
    @Transactional
    public void updatePriority() {
        IpMark ipMark = getMark();

        IpMarkPriorities priority = ipMark.getPriorities().get(0);
        entityManager.detach(priority);//detaching otherwise the primary key of the priority
        priority.setPriorityDate(DateUtils.localDateToDate(LocalDate.of(2020,1,1)));
        CfGeoCountry country = new CfGeoCountry();
        country.setCountryCode("JP");
        priority.setCountry(country);
        priority.setNotes("alabala");
        priority.getPk().setPriorityApplId("123456");
        priority.getPk().setCountryCode("JP");
        int originalSize = ipMark.getPriorities().size();

        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        Optional<IpMarkPriorities> updatedPriority = updated.getPriorities().stream().filter(p -> p.getNotes().equals("alabala")).findFirst();
        assertTrue(updatedPriority.isPresent());
        assertEquals("JP", updatedPriority.get().getCountry().getCountryCode());
        assertEquals(priority.getPk().getPriorityApplId(), updatedPriority.get().getPk().getPriorityApplId());
        assertEquals(originalSize, updated.getPriorities().size());

    }
    @Test
    @Transactional
    public void insertNewPriority() {
        IpMark ipMark = getMark();
        IpFilePK ipFilePK = ipMark.getPk();
        Assert.assertNotNull(ipMark);

        int prioritFirstSize = ipMark.getPriorities().size();
        IpMarkPriorities newPriority = new IpMarkPriorities();
        IpMarkPrioritiesPK pk = new IpMarkPrioritiesPK(ipFilePK.getFileSeq(), ipFilePK.getFileTyp(), ipFilePK.getFileSer(), ipFilePK.getFileNbr(), PRIORITY_COUNTRY, PRIORITY_APP_ID);
        newPriority.setPk(pk);
        newPriority.setPriorityDate(new Date());
        newPriority.setRowVersion(1);
        ipMark.getPriorities().add(newPriority);

        IpMark save = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(save.getPriorities().size(), (prioritFirstSize + 1));
    }

    @Test
    @Transactional
    public void deletePriority() {
        IpMark ipMark = getMark();
        Assert.assertNotNull(ipMark);

        List<IpMarkPriorities> ipMarkPriorities = ipMark.getPriorities();
        int oldSize = ipMarkPriorities.size();
        IpMarkPriorities removed = ipMarkPriorities.remove(ipMarkPriorities.size() - 1);
        IpMark updated = ipMarkRepository.saveAndFlush(ipMark);
        assertEquals(oldSize - 1, updated.getPriorities().size());
    }

}
