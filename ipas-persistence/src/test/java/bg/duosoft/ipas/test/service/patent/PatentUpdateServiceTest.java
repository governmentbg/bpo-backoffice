package bg.duosoft.ipas.test.service.patent;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.core.model.patent.CPctApplicationData;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.test.service.ServiceTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

import static bg.duosoft.ipas.test.TestUtils.generateRandomString;
import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) //poradi nqkakva prichina, ako ne se izchisti context-a sled kato se execute-ne klasa, posle gyrmqt testovete za Search-ovete!!!!
public class PatentUpdateServiceTest extends ServiceTestBase {

    @Autowired
    private PatentService patentService;

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private IpoSearchService searchService;
    @Autowired
    private IndexService indexService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        indexService.delete(IpPatent.class);
        indexService.index(new IpFilePK("BG", "P", 2014, 111719), IpPatent.class);
    }

    @After
    public void tearDown() {

    }
    private CPatent updatePatent(CPatent patent, boolean withDrawings) {
        patentService.updatePatent(patent);
        em.flush();
        em.clear();
        CFileId fileId = patent.getFile().getFileId();
        return patentService.findPatent(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), withDrawings);
    }


    private CPatent getPatentForInventorTests() {
        return patentService.findPatent("BG", "P", 2018, 112664, false);
    }

    private CPatent getPatentForIpcsClassesTests() {
        return patentService.findPatent("BG", "P", 2018, 112748, false);
    }

    private CPatent getPatentForClaimsTests() {
        return patentService.findPatent("BG", "P", 2004, 108514, false);
    }


    private CPatentIpcClass createNewCPatentIpcClass() {
        CPatentIpcClass newIpcClass = new CPatentIpcClass();
        newIpcClass.setIpcQualification("2");
        newIpcClass.setIpcEdition("8");
        newIpcClass.setIpcSection("F");
        newIpcClass.setIpcClass("42");
        newIpcClass.setIpcSubclass("B");
        newIpcClass.setIpcGroup("12");
        newIpcClass.setIpcSubgroup("18");
        return newIpcClass;
    }

    private void editCPatentIpcClass(CPatentIpcClass editedIpcClass) {
        editedIpcClass.setIpcQualification("2");
        editedIpcClass.setIpcEdition("8");
        editedIpcClass.setIpcSection("F");
        editedIpcClass.setIpcClass("42");
        editedIpcClass.setIpcSubclass("B");
        editedIpcClass.setIpcGroup("12");
        editedIpcClass.setIpcSubgroup("18");
    }


    private void editCPatentPctApplicationData(CPctApplicationData pctApplicationData) {
        pctApplicationData.setPctPhase(Long.valueOf(2));
        pctApplicationData.setPctPublicationId("WO2002053722");
        pctApplicationData.setPctPublicationCountryCode("BG");
        pctApplicationData.setPctApplicationId("IB2001000208");

    }

    private CPctApplicationData createCPatentPctApplicationData() {
        CPctApplicationData pctApplicationData = new CPctApplicationData();
        pctApplicationData.setPctPhase(Long.valueOf(2));
        pctApplicationData.setPctPublicationId("WO2002053722");
        pctApplicationData.setPctPublicationCountryCode("BG");
        pctApplicationData.setPctApplicationId("IB2001000208");
        return pctApplicationData;

    }

    private void compareCPatentPctApplicationData(CPctApplicationData pctApplicationData1, CPctApplicationData pctApplicationData2) {
        _assertEquals(pctApplicationData1, pctApplicationData2, CPctApplicationData::getPctPhase);
        _assertEquals(pctApplicationData1, pctApplicationData2, CPctApplicationData::getPctPublicationId);
        _assertEquals(pctApplicationData1, pctApplicationData2, CPctApplicationData::getPctPublicationCountryCode);
        _assertEquals(pctApplicationData1, pctApplicationData2, CPctApplicationData::getPctApplicationId);

    }


    private void compareIpcClasses(CPatentIpcClass cPatentIpcClass1, CPatentIpcClass cPatentIpcClass2) {
        _assertEquals(cPatentIpcClass1, cPatentIpcClass1, CPatentIpcClass::getIpcQualification);
        _assertEquals(cPatentIpcClass1, cPatentIpcClass1, CPatentIpcClass::getIpcQualification);
        _assertEquals(cPatentIpcClass1, cPatentIpcClass1, CPatentIpcClass::getIpcSection);
        _assertEquals(cPatentIpcClass1, cPatentIpcClass1, CPatentIpcClass::getIpcClass);
        _assertEquals(cPatentIpcClass1, cPatentIpcClass1, CPatentIpcClass::getIpcSubclass);
        _assertEquals(cPatentIpcClass1, cPatentIpcClass1, CPatentIpcClass::getIpcGroup);
        _assertEquals(cPatentIpcClass1, cPatentIpcClass1, CPatentIpcClass::getIpcSubgroup);
    }

    @Transactional
    @Test
    public void testRemoveInventor() {
        CPatent originalPatent = getPatentForInventorTests();
        assertEquals(2, originalPatent.getAuthorshipData().getAuthorList().size());
        originalPatent.getAuthorshipData().getAuthorList().remove(1);
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(1, originalPatent.getAuthorshipData().getAuthorList().size());
        assertEquals(1, updated.getAuthorshipData().getAuthorList().size());
        assertEquals(Integer.valueOf(280867), updated.getAuthorshipData().getAuthorList().get(0).getPerson().getPersonNbr());
        assertEquals(Integer.valueOf(2), updated.getAuthorshipData().getAuthorList().get(0).getPerson().getAddressNbr());
    }


    @Transactional
    @Test
    public void testUpdateInventor() {
        String randomString = generateRandomString(20);
        CPatent originalPatent = getPatentForInventorTests();
        originalPatent.getAuthorshipData().getAuthorList().get(0).getPerson().setPersonName(randomString);
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(originalPatent.getAuthorshipData().getAuthorList().size(), 2);
        assertEquals(updated.getAuthorshipData().getAuthorList().size(), 2);
        assertEquals(updated.getAuthorshipData().getAuthorList().get(0).getPerson().getPersonName(), randomString);
        assertEquals(originalPatent.getAuthorshipData().getAuthorList().get(0).getPerson().getPersonName(), randomString);
    }

    @Transactional
    @Test
    public void testAddInventor() {
        CPatent originalPatent = getPatentForInventorTests();
        assertEquals(2, originalPatent.getAuthorshipData().getAuthorList().size());
        originalPatent.getAuthorshipData().getAuthorList().add(createRandomCAuthor());
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(3, originalPatent.getAuthorshipData().getAuthorList().size());
        assertEquals(3, updated.getAuthorshipData().getAuthorList().size());
        compareCAuthors(originalPatent.getAuthorshipData().getAuthorList().get(2), updated.getAuthorshipData().getAuthorList().get(2));
    }


    @Transactional
    @Test
    public void testAddOwner() {
        CPatent originalPatent = patentService.findPatent("BG", "P", 2018, 112662, false);
        originalPatent.getFile().getOwnershipData().getOwnerList().add(createRandomCOwner());
        CPatent updated = updatePatent(originalPatent, false);
        compareCOwners(originalPatent.getFile().getOwnershipData().getOwnerList().get(1), updated.getFile().getOwnershipData().getOwnerList().get(1));
    }

    @Transactional
    @Test
    public void testUpdateOwner() {
        String randomString = generateRandomString(20);
        CPatent originalPatent = patentService.findPatent("BG", "P", 2018, 112663, false);
        originalPatent.getFile().getOwnershipData().getOwnerList().get(0).getPerson().setPersonName(randomString);
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(originalPatent.getFile().getOwnershipData().getOwnerList().size(), 1);
        assertEquals(updated.getFile().getOwnershipData().getOwnerList().size(), 1);
        assertEquals(updated.getFile().getOwnershipData().getOwnerList().get(0).getPerson().getPersonName(), randomString);
        assertEquals(originalPatent.getFile().getOwnershipData().getOwnerList().get(0).getPerson().getPersonName(), randomString);
    }

    @Transactional
    @Test
    public void testRemoveOwner() {
        CPatent originalPatent = patentService.findPatent("BG", "P", 2018, 112763, false);
        assertEquals(2, originalPatent.getFile().getOwnershipData().getOwnerList().size());
        originalPatent.getFile().getOwnershipData().getOwnerList().remove(1);
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(1, originalPatent.getFile().getOwnershipData().getOwnerList().size());
        assertEquals(1, updated.getFile().getOwnershipData().getOwnerList().size());
        assertEquals(Integer.valueOf(289480), updated.getFile().getOwnershipData().getOwnerList().get(0).getPerson().getPersonNbr());
        assertEquals(Integer.valueOf(1), updated.getFile().getOwnershipData().getOwnerList().get(0).getPerson().getAddressNbr());
    }


    @Transactional
    @Test
    public void testRemoveDrawing() {
        CPatent originalPatent = patentService.findPatent("BG", "P", 2015, 112103, true);
        assertEquals(1, originalPatent.getTechnicalData().getDrawingList().size());
        originalPatent.getTechnicalData().setDrawingList(new ArrayList<>());
        originalPatent.setIndReadDrawingList(false);
        originalPatent.setPatentContainsDrawingList(false);
        CPatent updated = updatePatent(originalPatent, true);
        assertEquals(0, updated.getTechnicalData().getDrawingList().size());
    }

    @Transactional
    @Test
    public void testRemoveIpcClass() {
        CPatent originalPatent = getPatentForIpcsClassesTests();
        assertEquals(1, originalPatent.getTechnicalData().getIpcClassList().size());
        originalPatent.getTechnicalData().setIpcClassList(new ArrayList<>());
        originalPatent.getTechnicalData().setHasIpc(false);
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(0, updated.getTechnicalData().getIpcClassList().size());
        assertEquals(0, originalPatent.getTechnicalData().getIpcClassList().size());
    }


    @Transactional
    @Test
    public void testAddIpcClass() {
        CPatent originalPatent = getPatentForIpcsClassesTests();
        assertEquals(1, originalPatent.getTechnicalData().getIpcClassList().size());
        originalPatent.getTechnicalData().getIpcClassList().add(createNewCPatentIpcClass());
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(originalPatent.getTechnicalData().getIpcClassList().size(), 2);
        assertEquals(updated.getTechnicalData().getIpcClassList().size(), 2);
        compareIpcClasses(updated.getTechnicalData().getIpcClassList().get(1), createNewCPatentIpcClass());
    }


    @Transactional
    @Test
    public void testUpdateIpcClass() {
        CPatent originalPatent = getPatentForIpcsClassesTests();
        assertEquals(1, originalPatent.getTechnicalData().getIpcClassList().size());
        editCPatentIpcClass(originalPatent.getTechnicalData().getIpcClassList().get(0));
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(originalPatent.getTechnicalData().getIpcClassList().size(), 1);
        assertEquals(updated.getTechnicalData().getIpcClassList().size(), 1);
        compareIpcClasses(updated.getTechnicalData().getIpcClassList().get(0), createNewCPatentIpcClass());
    }


    @Transactional
    @Test
    public void testRemoveClaims() {
        CPatent originalPatent = getPatentForClaimsTests();
        assertEquals(1, originalPatent.getTechnicalData().getClaimList().size());
        originalPatent.getTechnicalData().setClaimList(new ArrayList<>());
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(0, updated.getTechnicalData().getClaimList().size());
        assertEquals(0, originalPatent.getTechnicalData().getClaimList().size());
    }


    @Transactional
    @Test
    public void testUpdateClaims() {
        String randomStringForUS = generateRandomString(20);
        String randomStringForMX = generateRandomString(20);
        CPatent originalPatent = getPatentForClaimsTests();
        assertEquals(1, originalPatent.getTechnicalData().getClaimList().size());
        originalPatent.getTechnicalData().getClaimList().get(0).setClaimEnglishDescription(randomStringForUS);
        originalPatent.getTechnicalData().getClaimList().get(0).setClaimDescription(randomStringForMX);
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(originalPatent.getTechnicalData().getClaimList().size(), 1);
        assertEquals(updated.getTechnicalData().getClaimList().size(), 1);
        assertEquals(randomStringForUS, updated.getTechnicalData().getClaimList().get(0).getClaimEnglishDescription());
        assertEquals(randomStringForMX, updated.getTechnicalData().getClaimList().get(0).getClaimDescription());
    }

    @Transactional
    @Test
    public void testAddClaims() {
        String randomStringForUS = generateRandomString(20);
        String randomStringForMX = generateRandomString(20);
        Long claimNbr = Long.valueOf(1);
        CPatent originalPatent = getPatentForClaimsTests();
        assertEquals(1, originalPatent.getTechnicalData().getClaimList().size());
        originalPatent.getTechnicalData().getClaimList().add(new CClaim(claimNbr, randomStringForMX, randomStringForUS));
        CPatent updated = updatePatent(originalPatent, false);
        assertEquals(originalPatent.getTechnicalData().getClaimList().size(), 2);
        assertEquals(updated.getTechnicalData().getClaimList().size(), 2);
        assertEquals(randomStringForUS, updated.getTechnicalData().getClaimList().get(1).getClaimEnglishDescription());
        assertEquals(randomStringForMX, updated.getTechnicalData().getClaimList().get(1).getClaimDescription());
        assertEquals(claimNbr, updated.getTechnicalData().getClaimList().get(1).getClaimNbr());
    }


    @Transactional
    @Test
    public void testUpdatePctApplicationData() {
        CPatent originalPatent = patentService.findPatent("BG", "P", 2003, 107971, false);
        editCPatentPctApplicationData(originalPatent.getPctApplicationData());
        CPatent updated = updatePatent(originalPatent, false);
        compareCPatentPctApplicationData(updated.getPctApplicationData(), createCPatentPctApplicationData());

    }


}
