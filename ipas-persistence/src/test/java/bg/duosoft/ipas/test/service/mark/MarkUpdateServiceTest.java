package bg.duosoft.ipas.test.service.mark;

import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.file.*;
import bg.duosoft.ipas.core.model.mark.*;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.person.PersonService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.enums.MarkSignType;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.mark.IpName;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpNameRepository;
import bg.duosoft.ipas.test.service.ServiceTestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static bg.duosoft.ipas.test.repository.mark.IpNameRepositoryTest.MICROSOFT_MARK_CODE;
import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 26.3.2019 г.
 * Time: 17:10
 */
@Rollback
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) //poradi nqkakva prichina, ako ne se izchisti context-a sled kato se execute-ne klasa, posle gyrmqt testovete za Search-ovete!!!!
public class MarkUpdateServiceTest extends ServiceTestBase {
    @Autowired
    private MarkService markService;
    @Autowired
    private PersonService personService;
    @Autowired
    private IpNameRepository ipNameRepository;
    @Autowired
    private IpMarkRepository ipMarkRepository;
    @Autowired
    private IpoSearchService searchService;
    @Autowired
    private IndexService indexService;
    
    @Before
    public void setUp() {
        indexService.delete(IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2012, 114503), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2015, 114503), IpMark.class);
        indexService.index(new IpFilePK("BG", "N", 2013, 130130), IpMark.class);
        indexService.index(new IpFilePK("BG", "N", 2000, 50677), IpMark.class);
    }
    @After
    public void tearDown() {
    }

    //    private Supplier<CMark> markSupplier;
//
//    private CMark readMark() {
//        return markSupplier.get();
//    }
    private CMark updateMark(CMark mark) {
        markService.updateMark(mark);
        em.flush();
        em.clear();
        CFileId fileId = mark.getFile().getFileId();
        return markService.findMark(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), true);
    }

    @PersistenceContext
    private EntityManager em;

    private CMark getNameOnlyRegisterdNotPublishedWithoutRepresentativeWihouthPrioritiesWithNiceClassesWithoutViennaClassesTrademark() {
        //getNameOnlyRegisterdNotPublishedWithoutRepresentativeWihouthPrioritiesWithNiceClassesWithoutViennaClassesTrademark
        //name only mark
        //registered
        //not published
        //mark without representatives!
        //mark without priorities
        //mark with nice classes
        //without vienna classes
        return markService.findMark("BG", "N", 2017, 122196, true);
    }
    public CMark getBothNameAndLogoRegisteredNotPublishedWithRepresentativeWithoutPrioritiesWithNiceClassesWithoutViennaClassesTrademark() {
        return markService.findMark("BG", "N", 1952, 150030, true);
    }

    private CMark getBothNameAndLogoRegisteredWithRepresentativeNoPrioritiesWithNiceClassesWithViennaClassesTrademark() {
        //BothNameAndLogoRegisteredWithRepresentativeNoPrioritiesWithNiceClassesWithViennaClassesTrademark
        //Both name and logo (Bulgaria Design)
        //registered
        //one representative
        //no priorities
        //with 2 nice classes
        //with 2 vienna classes
        return markService.findMark("BG", "N", 2017, 144455, true);
    }

    private CMark getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark() {
        //name only mark
        //with publications
        //not registerd
        //with 1 representative
        //no priorities
        //two nice classes
        //no vienna classes
        return markService.findMark("BG", "N", 2017, 144482, true);
    }

    private CMark getNameOnlyRegisteredWithPrioritiesWithRepresentativeWithNiceClassesTrademark() {
        return markService.findMark("BG", "N", 1993, 23353, true);
    }

    private CMark getMarkWithRelationship1() {
        return markService.findMark("BG", "N", 2010, 114503, true);
    }

    private CMark getMarkWithRelationship2() {
        return markService.findMark("BG", "D", 2012, 114503, true);
    }


    @Test
    public void testAddPartnershipRepresentative() {
        CMark originalMark = getNameOnlyRegisterdNotPublishedWithoutRepresentativeWihouthPrioritiesWithNiceClassesWithoutViennaClassesTrademark();
        CPerson partner = personService.findPersons(CCriteriaPerson.builder().agentCode("A1").build()).get(0);
        CRepresentative representative = new CRepresentative();
        representative.setPerson(partner);
        representative.setRepresentativeType("PA");
        int originalRepresentativesSize = originalMark.getFile().getRepresentationData().getRepresentativeList().size();
        originalMark.getFile().getRepresentationData().getRepresentativeList().add(representative);

        CMark updated = updateMark(originalMark);
        assertEquals(partner, updated.getFile().getRepresentationData().getRepresentativeList().get(0).getPerson());
        assertEquals(originalRepresentativesSize + 1, updated.getFile().getRepresentationData().getRepresentativeList().size());
    }

    @Test
    public void testAddOwner() {
        CMark originalMark = getNameOnlyRegisterdNotPublishedWithoutRepresentativeWihouthPrioritiesWithNiceClassesWithoutViennaClassesTrademark();
        originalMark.getFile().getOwnershipData().getOwnerList().add(createRandomCOwner());
        CMark updated = updateMark(originalMark);
        compareCOwners(originalMark.getFile().getOwnershipData().getOwnerList().get(1), updated.getFile().getOwnershipData().getOwnerList().get(1));
    }



    @Test
    public void testRemoveLogo() {
        CMark originalMark = getBothNameAndLogoRegisteredWithRepresentativeNoPrioritiesWithNiceClassesWithViennaClassesTrademark();


        List<CMarkAttachment> attachments = originalMark.getSignData().getAttachments();
        assertFalse(CollectionUtils.isEmpty(attachments));
        CMarkAttachment logo = MarkSignDataAttachmentUtils.selectFirstImageAttachment(originalMark.getSignData());
        assertNotNull(logo);

        originalMark.getSignData().setAttachments(null);
        originalMark.getSignData().setSignType(MarkSignType.WORD);//changes the signType to Name only, because the nameAndLogo mark must have logo.
        CMark updated = updateMark(originalMark);

        CMarkAttachment markAttachment = MarkSignDataAttachmentUtils.selectFirstImageAttachment(updated.getSignData());
        assertEquals(null, markAttachment);
    }


    @Test
    public void testChangeMarkNameToNonExistingName() {
        CMark markToBeUpdated = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        String markName = "NON EXISTING MARK NAME123123";
        markToBeUpdated.getSignData().setMarkName(markName);
        markToBeUpdated.getSignData().setMarkNameInOtherLang(markName + "-other");
        CMark updated = updateMark(markToBeUpdated);
        assertEquals(markName, updated.getSignData().getMarkName());
        assertEquals(markName + "-other", updated.getSignData().getMarkNameInOtherLang());
        Integer lastIpNameId = ipNameRepository.getLastId();
        IpName ipName = ipNameRepository.getOne(lastIpNameId);
        assertEquals(updated.getSignData().getMarkName(), ipName.getMarkName());
        assertEquals(updated.getSignData().getMarkNameInOtherLang(), ipName.getMarkNameLang2());
    }


    @Test
    public void testChangeMarkNameToMicrosoftName() {
        CMark markToBeUpdated = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        String markName = "MICROSOFT";
        markToBeUpdated.getSignData().setMarkName(markName);
        CMark updated = updateMark(markToBeUpdated);
        IpMark ipMark = ipMarkRepository.getOne(new IpFilePK("BG", "N", 2017, 144482));


        assertEquals(markName, updated.getSignData().getMarkName());
        assertEquals((Object) MICROSOFT_MARK_CODE, ipMark.getName().getMarkCode());
    }


    @Test

    public void addNiceClass() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        assertTrue(mark.getProtectionData().getNiceClassList().stream().filter(r -> r.getNiceClassNbr() == 1).count() == 0);//the mark do not contain nice class with number = 1
        int originalListCount = mark.getProtectionData().getNiceClassList().size();
        CNiceClass niceClass = new CNiceClass();
        niceClass.setNiceClassDescription("авторска бижутерия; бижута за глава; бижута за шапки; бижута, игли за ревери; бижута, изработени от неблагородни метали; бижута, изработени от полускъпоценни камъни; бижута, часовници");
        niceClass.setNiceClassNbr(1);
        niceClass.setNiceClassEdition(0);
        niceClass.setNiceClassVersion("0");
        niceClass.setNiceClassDetailedStatus("R");
        niceClass.setAllTermsDeclaration(true);
        mark.getProtectionData().getNiceClassList().add(niceClass);
        CMark updated = updateMark(mark);
        assertEquals(originalListCount + 1, updated.getProtectionData().getNiceClassList().size());
        CNiceClass addedNiceClass = updated.getProtectionData().getNiceClassList().stream().filter(r -> r.getNiceClassNbr() == 1).findFirst().get();//finds the nice class with number 1
//        _assertEquals(niceClass, addedNiceClass, CNiceClass::getNiceClassNbr);
        _assertEquals(niceClass, addedNiceClass, CNiceClass::getNiceClassEdition);
        _assertEquals(niceClass, addedNiceClass, CNiceClass::getNiceClassVersion);
        _assertEquals(niceClass, addedNiceClass, CNiceClass::getNiceClassDetailedStatus);
        _assertEquals(niceClass, addedNiceClass, CNiceClass::getAllTermsDeclaration);
    }

    @Test

    public void testRemoveNiceClass() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        int originalListCount = mark.getProtectionData().getNiceClassList().size();
        CNiceClass cls = mark.getProtectionData().getNiceClassList().get(0);
        mark.getProtectionData().getNiceClassList().remove(cls);
        CMark updated = updateMark(mark);
        assertEquals(originalListCount, updated.getProtectionData().getNiceClassList().size() + 1);
    }

    @Test

    public void testChangeMadridApplication() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CMadridApplicationData madridApplication = mark.getMadridApplicationData();
        madridApplication.setInternationalFileNumber("Test");
        madridApplication.setBasicFileRef("test");
        CMark updated = updateMark(mark);

        assertEquals(madridApplication.getInternationalFileNumber(), updated.getMadridApplicationData().getInternationalFileNumber());
        assertEquals(madridApplication.getBasicFileRef(), updated.getMadridApplicationData().getBasicFileRef());
    }

    @Test

    public void testChangeSignBasicData() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CSignData signData = mark.getSignData();
        signData.setMarkName("test");
        signData.setMarkNameInOtherLang("test-other");
        signData.setMarkTranslation("translation");
        signData.setMarkTranslationInOtherLang("translation-other");
        signData.setMarkTransliteration("transliteration");
        signData.setMarkTransliterationInOtherLang("transliteration-other");
        CMark updated = updateMark(mark);
        CSignData updatedSignData = updated.getSignData();
        _assertEquals(signData, updatedSignData, CSignData::getMarkName);
        _assertEquals(signData, updatedSignData, CSignData::getMarkNameInOtherLang);
        _assertEquals(signData, updatedSignData, CSignData::getMarkTranslation);
        _assertEquals(signData, updatedSignData, CSignData::getMarkTranslationInOtherLang);
        _assertEquals(signData, updatedSignData, CSignData::getMarkTransliteration);
        _assertEquals(signData, updatedSignData, CSignData::getMarkTransliterationInOtherLang);
    }

    @Test

    public void testChangeLimitationData() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CLimitationData limitationData = mark.getLimitationData();
        limitationData.setDisclaimer("disclaimer");
        limitationData.setByConsent("by-consent");
        limitationData.setDisclaimerInOtherLang("disclaimer-other");
        limitationData.setRegulations("regulations");
        CMark updated = updateMark(mark);
        CLimitationData updateLimitationData = updated.getLimitationData();
        _assertEquals(limitationData, updateLimitationData, CLimitationData::getByConsent);
        _assertEquals(limitationData, updateLimitationData, CLimitationData::getDisclaimer);
        _assertEquals(limitationData, updateLimitationData, CLimitationData::getDisclaimerInOtherLang);
        _assertEquals(limitationData, updateLimitationData, CLimitationData::getRegulations);
    }

    @Test

    public void testChangeRenewalData() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CRenewalData renewalData = mark.getRenewalData();
        renewalData.setLastRenewalDate(DateUtils.localDateToDate(LocalDate.of(2018, 1, 1)));
        CMark updated = updateMark(mark);
        assertEquals(renewalData.getLastRenewalDate(), updated.getRenewalData().getLastRenewalDate());

    }

    @Test

    public void testAddTransformation() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CRelationshipExtended transformation = mark.getRelationshipExtended();
        assertNull(transformation);
        transformation = new CRelationshipExtended();
        transformation.setCancellationDate(DateUtils.localDateToDate(LocalDate.of(2017, 1, 1)));
        transformation.setFilingDate(DateUtils.localDateToDate(LocalDate.of(2016, 1, 1)));
        transformation.setPriorityDate(DateUtils.localDateToDate(LocalDate.of(2015, 1, 1)));
        transformation.setRegistrationDate(DateUtils.localDateToDate(LocalDate.of(2016, 5, 1)));
        transformation.setFilingNumber("test");
        transformation.setApplicationType("EM");
        transformation.setRelationshipType("ТМ");
        mark.setRelationshipExtended(transformation);
        CMark updated = updateMark(mark);
        CRelationshipExtended updatedTransformation = updated.getRelationshipExtended();
        assertNotNull(updatedTransformation);
        _assertEquals(transformation, updatedTransformation, CRelationshipExtended::getApplicationType);
        _assertEquals(transformation, updatedTransformation, CRelationshipExtended::getFilingNumber);
        _assertEquals(transformation, updatedTransformation, CRelationshipExtended::getRegistrationDate);
        _assertEquals(transformation, updatedTransformation, CRelationshipExtended::getPriorityDate);
        _assertEquals(transformation, updatedTransformation, CRelationshipExtended::getFilingDate);
        _assertEquals(transformation, updatedTransformation, CRelationshipExtended::getCancellationDate);
    }

    @Test

    public void testChangeFileBaseData() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CFile file = mark.getFile();
        file.setNotes("test");
        CMark updated = updateMark(mark);
        assertEquals(mark.getFile().getNotes(), updated.getFile().getNotes());
    }

    @Test

    public void testChangeBasePriorityData() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CPriorityData priorityData = mark.getFile().getPriorityData();
        priorityData.setHasExhibitionData(true);
        priorityData.setExhibitionDate(DateUtils.localDateToDate(LocalDate.of(2000, 1, 1)));
        priorityData.setExhibitionNotes("exhibition-notes");
        priorityData.setEarliestAcceptedParisPriorityDate(DateUtils.localDateToDate(LocalDate.of(2001, 1, 1)));
        CMark updated = updateMark(mark);
        CPriorityData updatedPriority = updated.getFile().getPriorityData();
        _assertEquals(priorityData, updatedPriority, CPriorityData::getEarliestAcceptedParisPriorityDate);
        _assertEquals(priorityData, updatedPriority, CPriorityData::getExhibitionDate);
        _assertEquals(priorityData, updatedPriority, CPriorityData::getExhibitionNotes);

    }

    @Test

    public void testAddParisPriority() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CPriorityData priorityData = mark.getFile().getPriorityData();
        List<CParisPriority> parisPriorities = priorityData.getParisPriorityList();
        CParisPriority pp = new CParisPriority();
        pp.setApplicationId("112312312312313");
        pp.setCountryCode("ES");
        pp.setNotes("test-notes");
        pp.setPriorityDate(DateUtils.localDateToDate(LocalDate.of(2000, 1, 1)));
        pp.setPriorityStatus(1);
        int originalSize = parisPriorities.size();
        parisPriorities.add(pp);
        mark.getFile().getPriorityData().setHasParisPriorityData(parisPriorities.size() > 0);
        CMark updated = updateMark(mark);
        List<CParisPriority> updatedParisPriorities = updated.getFile().getPriorityData().getParisPriorityList();
        assertEquals(originalSize + 1, updatedParisPriorities.size());
        CParisPriority insertedPp = updatedParisPriorities.get(updatedParisPriorities.size() - 1);
        _assertEquals(pp, insertedPp, CParisPriority::getPriorityDate);
        _assertEquals(pp, insertedPp, CParisPriority::getCountryCode);
        _assertEquals(pp, insertedPp, CParisPriority::getPriorityStatus);
        _assertEquals(pp, insertedPp, CParisPriority::getApplicationId);
        _assertEquals(pp, insertedPp, CParisPriority::getNotes);

    }

    @Test

    public void testRemoveParisPriority() {
        CMark mark = getNameOnlyRegisteredWithPrioritiesWithRepresentativeWithNiceClassesTrademark();
        List<CParisPriority> ppData = mark.getFile().getPriorityData().getParisPriorityList();
        int originalSize = ppData.size();
        ppData.remove(originalSize - 1);
        CMark updated = updateMark(mark);
        assertEquals(originalSize - 1, updated.getFile().getPriorityData().getParisPriorityList().size());
    }

    @Test

    public void testChangeFileFilingData() {
        CMark mark = getNameOnlyRegisteredWithPrioritiesWithRepresentativeWithNiceClassesTrademark();
        CFilingData filingData = mark.getFile().getFilingData();
        filingData.setFilingDate(DateUtils.localDateToDate(LocalDate.of(2000, 1, 1)));
        filingData.setIndManualInterpretationRequired(true);
        filingData.setNovelty1Date(DateUtils.localDateToDate(LocalDate.of(2001, 1, 1)));
        filingData.setNovelty2Date(DateUtils.localDateToDate(LocalDate.of(2002, 1, 1)));
        filingData.setCaptureDate(DateUtils.localDateToDate(LocalDate.of(2003, 1, 1)));
        filingData.setCaptureUserId(Objects.equals(filingData.getCaptureUserId(), 1l) ? 4l : 1l);
        filingData.setExternalOfficeCode("123");
        filingData.setExternalOfficeFilingDate(DateUtils.localDateToDate(LocalDate.of(2013, 1, 1)));
        filingData.setExternalSystemId("1");
        filingData.setReceptionDate(DateUtils.localDateToDate(LocalDate.of(2011, 1, 1)));
        filingData.setReceptionUserId(Objects.equals(filingData.getCaptureUserId(), 1l) ? 4l : 1l);
        filingData.setValidationUserId(Objects.equals(filingData.getCaptureUserId(), 1l) ? 4l : 1l);
        filingData.setValidationDate(DateUtils.localDateToDate(LocalDate.of(2016, 1, 1)));
        filingData.setCorrFileNbr(130130l);
        filingData.setCorrFileSeq("BG");
        filingData.setCorrFileSeries(2013l);
        filingData.setCorrFileType("N");
        filingData.setIndIncorrRecpDeleted("S");
        CMark updated = updateMark(mark);
        CFilingData updatedFilingData = updated.getFile().getFilingData();
        assertNotSame(filingData, updatedFilingData);
        _assertEquals(filingData, updatedFilingData, CFilingData::getFilingDate);
        _assertEquals(filingData, updatedFilingData, CFilingData::getIndManualInterpretationRequired);
        _assertEquals(filingData, updatedFilingData, CFilingData::getNovelty1Date);
        _assertEquals(filingData, updatedFilingData, CFilingData::getNovelty2Date);
        _assertEquals(filingData, updatedFilingData, CFilingData::getCaptureDate);
        _assertEquals(filingData, updatedFilingData, CFilingData::getCaptureUserId);
        _assertEquals(filingData, updatedFilingData, CFilingData::getExternalOfficeCode);
        _assertEquals(filingData, updatedFilingData, CFilingData::getExternalOfficeFilingDate);
        _assertEquals(filingData, updatedFilingData, CFilingData::getExternalSystemId);
        _assertEquals(filingData, updatedFilingData, CFilingData::getReceptionDate);
        _assertEquals(filingData, updatedFilingData, CFilingData::getReceptionUserId);
        _assertEquals(filingData, updatedFilingData, CFilingData::getValidationUserId);
        _assertEquals(filingData, updatedFilingData, CFilingData::getValidationDate);
        _assertEquals(filingData, updatedFilingData, CFilingData::getCorrFileSeq);
        _assertEquals(filingData, updatedFilingData, CFilingData::getCorrFileType);
        _assertEquals(filingData, updatedFilingData, CFilingData::getCorrFileSeries);
        _assertEquals(filingData, updatedFilingData, CFilingData::getCorrFileNbr);
        _assertEquals(filingData, updatedFilingData, CFilingData::getIndIncorrRecpDeleted);
    }

    @Test

    public void testChangeFileRegistrationData() {
        CMark mark = getNameOnlyRegisteredWithPrioritiesWithRepresentativeWithNiceClassesTrademark();
        CRegistrationData registrationData = mark.getFile().getRegistrationData();
        registrationData.setEntitlementDate(DateUtils.localDateToDate(LocalDate.of(2017, 1, 1)));
        registrationData.setExpirationDate(DateUtils.localDateToDate(LocalDate.of(2016, 1, 1)));
        registrationData.setRegistrationDate(DateUtils.localDateToDate(LocalDate.of(2015, 1, 1)));
        registrationData.setIndRegistered(true);

        CRegistrationId regId = new CRegistrationId();
        regId.setRegistrationDup("D");
        regId.setRegistrationNbr(123l);
        regId.setRegistrationSeries(2013l);
        regId.setRegistrationType("X");
        registrationData.setRegistrationId(regId);
        CMark updated = updateMark(mark);
        CRegistrationData updatedRegistrationData = updated.getFile().getRegistrationData();
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getIndRegistered);
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getRegistrationDate);
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getEntitlementDate);
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getExpirationDate);
        _assertEquals(regId, updatedRegistrationData.getRegistrationId(), CRegistrationId::getRegistrationDup);
        _assertEquals(regId, updatedRegistrationData.getRegistrationId(), CRegistrationId::getRegistrationNbr);
        _assertEquals(regId, updatedRegistrationData.getRegistrationId(), CRegistrationId::getRegistrationSeries);
        _assertEquals(regId, updatedRegistrationData.getRegistrationId(), CRegistrationId::getRegistrationType);

    }

    @Test

    public void testRemoveRegistrationData() {
        CMark mark = getNameOnlyRegisteredWithPrioritiesWithRepresentativeWithNiceClassesTrademark();
        CRegistrationData registrationData = mark.getFile().getRegistrationData();
//        registrationData.setEntitlementDate(null);//entitlementDate cannot be removed!!!
//        registrationData.setExpirationDate(null);//expirationDate cannot be removed!!!
        registrationData.setRegistrationDate(null);
        registrationData.setIndRegistered(false);
        registrationData.setRegistrationId(null);

        CMark updated = updateMark(mark);
        CRegistrationData updatedRegistrationData = updated.getFile().getRegistrationData();
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getIndRegistered);
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getRegistrationDate);
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getEntitlementDate);
        _assertEquals(registrationData, updatedRegistrationData, CRegistrationData::getExpirationDate);

        assertNull(updated.getFile().getRegistrationData().getRegistrationId().getRegistrationDup());
        assertNull(updated.getFile().getRegistrationData().getRegistrationId().getRegistrationSeries());
        assertNull(updated.getFile().getRegistrationData().getRegistrationId().getRegistrationType());
        assertNull(updated.getFile().getRegistrationData().getRegistrationId().getRegistrationNbr());

    }

    @Test

    public void testAddRelationship1() {
        _addRelationship(1, new CFileId("BG", "N", 2013, 130130));
    }


    @Test

    public void testAddRelationship2() {
        _addRelationship(2, new CFileId("BG", "N", 2000, 50677));
    }

    void _addRelationship(int type, CFileId relFileId) {
        CMark mark = getMarkWithRelationship1();
        List<CRelationship> relations = mark.getFile().getRelationshipList();
        long relations1size = relations.stream().filter(r -> r.getRelationshipRole().equals("1")).count();
        long relations2size = relations.stream().filter(r -> r.getRelationshipRole().equals("2")).count();
        CRelationship newRelationship = new CRelationship();
        newRelationship.setRelationshipRole(type + "");
        newRelationship.setRelationshipType("РНМ");
        newRelationship.setFileId(relFileId);
        relations.add(newRelationship);
        CMark updated = updateMark(mark);
        long newSize1 = relations1size + (type == 1 ? 1 : 0);
        long newSize2 = relations2size + (type == 2 ? 1 : 0);

        assertEquals(newSize1, updated.getFile().getRelationshipList().stream().filter(r -> r.getRelationshipRole().equals("1")).count());
        assertEquals(newSize2, updated.getFile().getRelationshipList().stream().filter(r -> r.getRelationshipRole().equals("2")).count());

        CRelationship insertedRel = updated.getFile().getRelationshipList().get(updated.getFile().getRelationshipList().size() - 1);
        _assertEquals(newRelationship, insertedRel, CRelationship::getRelationshipType);
        _assertEquals(newRelationship, insertedRel, CRelationship::getRelationshipRole);
        _assertEquals(relFileId, insertedRel.getFileId(), CFileId::getFileType);
        _assertEquals(relFileId, insertedRel.getFileId(), CFileId::getFileNbr);
        _assertEquals(relFileId, insertedRel.getFileId(), CFileId::getFileSeq);
        _assertEquals(relFileId, insertedRel.getFileId(), CFileId::getFileSeries);
    }

    @Test

    public void testRemoveRelationship1() {
        CMark mark = getMarkWithRelationship1();
        List<CRelationship> relations = mark.getFile().getRelationshipList();
        int originalSize = relations.size();
        relations.remove(relations.size() - 1);
        CMark updated = updateMark(mark);
        assertEquals(originalSize - 1, updated.getFile().getRelationshipList().size());
    }

    @Test

    public void testRemoveAllRelationships() {
        CMark mark = getMarkWithRelationship1();
        mark.getFile().setRelationshipList(new ArrayList<>());
        CMark updated = updateMark(mark);
        if (updated.getFile().getRelationshipList() != null) {
            assertEquals(0, updated.getFile().getRelationshipList().size());
        }
    }

    @Test

    public void testChangePublicationData() {
        CMark mark = getNameOnlyNotRegisterdWithPublicationsWithRepresentativeNoprioritiesWithNiceClassesWithNoViennaClassesTradeMark();
        CPublicationData publicationData = mark.getFile().getPublicationData();
        publicationData.setPublicationNotes("publication-notes");
        publicationData.setPublicationDate(DateUtils.localDateToDate(LocalDate.of(2000, 1, 1)));
        publicationData.setJournalCode("201208");
        CMark updated = updateMark(mark);
        CPublicationData updatedPublication = updated.getFile().getPublicationData();
        _assertEquals(publicationData, updatedPublication, CPublicationData::getJournalCode);
        _assertEquals(publicationData, updatedPublication, CPublicationData::getPublicationDate);
        _assertEquals(publicationData, updatedPublication, CPublicationData::getPublicationNotes);
    }

    @Test
    
    public void testChangeServicePersonFromRepresentativeToOwner() {
        CMark mark = getNameOnlyRegisteredWithPrioritiesWithRepresentativeWithNiceClassesTrademark();
        CPerson repr = mark.getFile().getRepresentationData().getRepresentativeList().get(0).getPerson();
        CPerson owner = mark.getFile().getOwnershipData().getOwnerList().get(0).getPerson();
        CPerson servicePerson = mark.getFile().getServicePerson();
        CPerson newServicePerson;
        if (Objects.equals(repr.getPersonNbr(), servicePerson.getPersonNbr())) {
            newServicePerson = owner;
        } else {
            newServicePerson = repr;
        }

        mark.getFile().setServicePerson(newServicePerson);
        assertNotEquals(servicePerson, newServicePerson);
        CMark updated = updateMark(mark);
        assertEquals(newServicePerson, updated.getFile().getServicePerson());

    }
}
