package bg.duosoft.ipas.test.service.patent;


import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.test.TestUtils;
import bg.duosoft.ipas.test.service.ServiceTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PatentServiceTest extends ServiceTestBase {


    @Autowired
    private PatentService patentService;
    @Autowired
    private IpPatentRepository ipPatentRepository;
    @PersistenceContext
    private EntityManager em;

    private CPatent updatePatent(CPatent patent, boolean withDrawings) {
        patentService.updatePatent(patent);
        em.flush();
        em.clear();
        CFileId fileId = patent.getFile().getFileId();
        return patentService.findPatent(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), withDrawings);
    }

    @Test
    @Transactional
    public void testDeletePatent() {
        CFileId fileId = new CFileId("BG", "P", 2015, 112162);
        CPatent p = patentService.findPatent(fileId, false);
        assertNotNull(p);
        patentService.deletePatent(fileId);
        em.flush();
        em.clear();
        p = patentService.findPatent(fileId, false);
        assertNull(p);
    }

//    @Test
//    @Transactional
//    @Commit
//    public void testUpdatePatentOwners() {
//
//        CPatent cPatentForUpdate = patentService.findPatent("BG", "P", 1952, 11, true);
//
//        for (int i = 0; i < cPatentForUpdate.getFile().getOwnershipData().getOwnerList().size(); i++) {
//            CPerson p = cPatentForUpdate.getFile().getOwnershipData().getOwnerList().get(i).getPerson();
//            updateDataForCPerson(p);
//
//        }
//        patentService.updatePatent(cPatentForUpdate);
//
//        CPatent cPatentAfterUpdate = patentService.findPatent("BG", "P", 1952, 11, true);
//
//        compareAddressAndPersonDataOriginAfterUpdate(cPatentForUpdate.getFile().getOwnershipData().getOwnerList().get(0).getPerson()
//                , cPatentAfterUpdate.getFile().getOwnershipData().getOwnerList().get(0).getPerson());
//
//
//    }

//    @Test
//    @Transactional
//    @Commit
//    public void testUpdatePatentRepresentatives() {
//
//        CPatent cPatentForUpdate = patentService.findPatent("BG", "P", 2005, 109399, true);
//
//        for (int i = 0; i < cPatentForUpdate.getFile().getRepresentationData().getRepresentativeList().size(); i++) {
//            CPerson p = cPatentForUpdate.getFile().getRepresentationData().getRepresentativeList().get(i).getPerson();
//            updateDataForCPerson(p);
//        }
//
//        patentService.updatePatent(cPatentForUpdate);
//
//        CPatent cPatentAfterUpdate = patentService.findPatent("BG", "P", 2005, 109399, true);
//
//        List<CRepresentative> cRepresentativeListForUpdate = cPatentForUpdate.getFile().getRepresentationData().getRepresentativeList();
//        List<CRepresentative> cRepresentativeListAfterUpdate = cPatentAfterUpdate.getFile().getRepresentationData().getRepresentativeList();
//
//        CPerson personForUpdate = cRepresentativeListForUpdate.get(0).getRepresentativeType().equals("AG")
//                ? cRepresentativeListForUpdate.get(0).getPerson() : cRepresentativeListForUpdate.get(1).getPerson();
//
//        CPerson personAfterUpdate = cRepresentativeListAfterUpdate.get(0).getRepresentativeType().equals("AG")
//                ? cRepresentativeListAfterUpdate.get(0).getPerson() : cRepresentativeListAfterUpdate.get(1).getPerson();
//
//        compareAddressAndPersonDataOriginAfterUpdate(personForUpdate, personAfterUpdate);
//
//    }

    @Test
    @Transactional
    public void testUpdatePatentInventors() {

        CPatent cPatentForUpdate = patentService.findPatent("BG", "P", 2015, 112160, true);

        for (int i = 0; i < cPatentForUpdate.getAuthorshipData().getAuthorList().size(); i++) {
            CPerson p = cPatentForUpdate.getAuthorshipData().getAuthorList().get(i).getPerson();
            updateDataForCPerson(p);
        }

        CPatent cPatentAfterUpdate = updatePatent(cPatentForUpdate, true);

        compareAddressAndPersonDataOriginAfterUpdate(cPatentForUpdate.getAuthorshipData().getAuthorList().get(0).getPerson()
                , cPatentAfterUpdate.getAuthorshipData().getAuthorList().get(0).getPerson());

    }


    private void updateDataForCPerson(CPerson p) {

        String testString = TestUtils.generateRandomString(18);
        p.setPersonName(testString);
        p.setAddressStreet(testString);
        p.setLegalNature(testString);
        p.setZipCode(testString.substring(0, 2));
        p.setStateName(testString);
        p.setAddressZone(testString);
        p.setEmail(testString);
        p.setTelephone(testString);
        p.setCityCode(testString.substring(0, 2));
        p.setCityName(testString);

    }

}




