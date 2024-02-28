package bg.duosoft.ipas.test.repository.proc;

import bg.duosoft.ipas.persistence.model.entity.process.IpProcResponsibleUserChanges;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcResponsibleUserChangesPK;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpProcResponsibleUserChangesRepositoryCustom;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcResponsibleUserChangesRepository;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: Georgi
 * Date: 5.11.2020 г.
 * Time: 17:52
 */
public class IpProcResponsibleUserChangesRepositoryTest extends TestBase {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private IpProcResponsibleUserChangesRepository responsibleUserChangesRepository;
    @Test
    @Transactional
    public void insertResponsibleUserLogChange() {
        responsibleUserChangesRepository.addResponsibleUserChanges(4, "2", 121392, 4, 1002);
        entityManager.flush();
        entityManager.clear();
        IpProcResponsibleUserChanges rec = responsibleUserChangesRepository.getOne(new IpProcResponsibleUserChangesPK("2", 121392, 1));
        assertNotNull(rec);
        Assert.assertEquals((Integer) 4, rec.getOldResponsibleUserId());
        Assert.assertEquals((Integer) 1002, rec.getNewResponsibleUserId());
        Assert.assertEquals((Integer) 4, rec.getUserChanged());

        responsibleUserChangesRepository.addResponsibleUserChanges(4, "2", 121392, 1002, 4);
        rec = responsibleUserChangesRepository.getOne(new IpProcResponsibleUserChangesPK("2", 121392, 2));
        assertNotNull(rec);
        assertEquals((Integer)1002, rec.getOldResponsibleUserId());
        assertEquals((Integer)4, rec.getNewResponsibleUserId());
        assertEquals((Integer)4, rec.getUserChanged());
    }
}
