package bg.duosoft.ipas.test.service.userdoc;

import bg.duosoft.ipas.core.model.document.CDoc;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CUser;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPersonData;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.userdoc.UserdocService;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.userdoc.UserdocPersonUtils;
import org.hibernate.search.annotations.DocumentId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


public class UserdocUpdateServiceTest extends TestBase {

    private static CDocumentId DEFAULT_USERDOC_ID = new CDocumentId("BG", "E", 2014, 984069);

    @Autowired
    private UserdocService userdocService;
    @Autowired
    private PersonService personService;


    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void changeUserdocType() {
        String newUserdocType = "ЖО";

        CUserdoc userdoc = userdocService.findUserdoc("BG", "E", 2014, 984392);
        assertNotNull(userdoc);
        assertNotNull(userdoc.getUserdocType());

        userdocService.changeUserdocType(userdoc.getDocumentId(), newUserdocType);
        entityManager.flush();
        entityManager.clear();
        CUserdoc newUserdoc = userdocService.findUserdoc("BG", "E", 2014, 984392);

        assertEquals(newUserdoc.getUserdocType().getUserdocType(), newUserdocType);
    }
    @Test
    @Transactional
    public void updateUserdocMainData() {
        CUserdoc userdoc = getUserdoc(DEFAULT_USERDOC_ID);
        userdoc.setNotes("Test Notes");
        userdoc.setApplicantNotes("ApplicantNotes");
        userdoc.setCaptureDate(DateUtils.convertToDate(LocalDate.of(2000, 1, 1)));
        userdoc.setCourtDocDate(DateUtils.convertToDate(LocalDate.of(2001, 1, 3)));
        userdoc.setDecreeDate(DateUtils.convertToDate(LocalDate.of(2002, 2, 3)));
        userdoc.setCaptureUser(new CUser("X", 100, true));
        userdoc.setCourtDocNbr(500);
        userdocService.updateUserdoc(userdoc, false);
        entityManager.flush();
        entityManager.clear();
        CUserdoc newUserdoc = getUserdoc(userdoc.getDocumentId());
        _assertEquals(userdoc, newUserdoc, CUserdoc::getApplicantNotes);
        _assertEquals(userdoc, newUserdoc, CUserdoc::getNotes);
        _assertEquals(userdoc, newUserdoc, CUserdoc::getCaptureDate);
        _assertEquals(userdoc, newUserdoc, CUserdoc::getCourtDocDate);
        _assertEquals(userdoc, newUserdoc, CUserdoc::getDecreeDate);
        _assertEquals(userdoc.getCaptureUser(), newUserdoc.getCaptureUser(), CUser::getUserId);
        _assertEquals(userdoc, newUserdoc, CUserdoc::getCourtDocNbr);
    }
    @Test
    @Transactional
    public void testAddGrantee() {
        CUserdoc userdoc = getUserdoc(DEFAULT_USERDOC_ID);
        CUserdocPersonData personData = userdoc.getUserdocPersonData();
        List<CUserdocPerson> personList = personData.getPersonList();
        int originalSize = personList.size();
        CPerson p = personService.selectPersonByPersonNumberAndAddressNumber(397892, 1);
        CUserdocPerson userdocPerson = UserdocPersonUtils.convertToUserdocPerson(p, UserdocPersonRole.GRANTEE, null, "notes",null);
        personList.add(userdocPerson);
        CUserdoc newUserdoc = updateUserdoc(userdoc);
        assertEquals(originalSize + 1, newUserdoc.getUserdocPersonData().getPersonList().size());
    }
    @Test
    @Transactional
    public void testRemovePerson() {
        CUserdoc userdoc = getUserdoc(DEFAULT_USERDOC_ID);
        CUserdocPersonData personData = userdoc.getUserdocPersonData();
        List<CUserdocPerson> personList = personData.getPersonList();
        int originalSize = personList.size();
        personList.remove(personList.size() - 1);
        CUserdoc newUserdoc = updateUserdoc(userdoc);
        assertEquals(originalSize - 1, newUserdoc.getUserdocPersonData().getPersonList().size());
    }
    @Test
    @Transactional
    public void testUpdateServicePerson() {
        CUserdoc userdoc = getUserdoc(DEFAULT_USERDOC_ID);
        userdoc.setServicePerson(personService.selectPersonByPersonNumberAndAddressNumber(397892, 1));
        CUserdoc newUserdoc = updateUserdoc(userdoc);
        assertEquals((Object)397892, newUserdoc.getServicePerson().getPersonNbr());
        assertEquals((Object)1, newUserdoc.getServicePerson().getAddressNbr());
    }
    @Test
    @Transactional
    public void testAddNiceClass() {
        CNiceClass nc = new CNiceClass();
        nc.setNiceClassNbr(35);
        nc.setNiceClassDetailedStatus("R");
        nc.setNiceClassGlobalStatus("R");
        nc.setNiceClassEdition(0);
        nc.setNiceClassVersion("0");
        nc.setNiceClassDescription("alabala");
        CUserdoc userdoc = getUserdoc(DEFAULT_USERDOC_ID);
        int originalSize = userdoc.getProtectionData().getNiceClassList().size();
        userdoc.getProtectionData().getNiceClassList().add(nc);

        CUserdoc newUserdoc = updateUserdoc(userdoc);
        assertEquals(originalSize + 1, userdoc.getProtectionData().getNiceClassList().size());
        Optional<CNiceClass> insertedNiceClass = userdoc.getProtectionData().getNiceClassList().stream().filter(r -> r.getNiceClassNbr() == 35).findFirst();
        assertTrue(insertedNiceClass.isPresent());
        _assertEquals(nc, insertedNiceClass.get(), CNiceClass::getNiceClassDescription);
        _assertEquals(nc, insertedNiceClass.get(), CNiceClass::getNiceClassVersion);
        _assertEquals(nc, insertedNiceClass.get(), CNiceClass::getNiceClassEdition);
        _assertEquals(nc, insertedNiceClass.get(), CNiceClass::getNiceClassGlobalStatus);
        _assertEquals(nc, insertedNiceClass.get(), CNiceClass::getNiceClassDetailedStatus);
    }

    private CUserdoc getUserdoc(CDocumentId documentId) {
        CUserdoc userdoc = userdocService.findUserdoc(documentId);
        return userdoc;
    }
    private CUserdoc updateUserdoc(CUserdoc ud) {
        userdocService.updateUserdoc(ud, false);
        entityManager.flush();
        entityManager.clear();
        CUserdoc newUserdoc = getUserdoc(ud.getDocumentId());
        return newUserdoc;
    }
    @Transactional
    @Test
    public void deleteUserdodc() {
        CDocumentId docId = new CDocumentId("BG", "E", 1981, 1328353);
        CUserdoc ud = userdocService.findUserdoc(docId);
        assertNotNull(ud);
        userdocService.deleteUserdoc(docId, false);
        entityManager.flush();
        entityManager.clear();
        ud = userdocService.findUserdoc(docId);

        assertNull(ud);
    }
}
