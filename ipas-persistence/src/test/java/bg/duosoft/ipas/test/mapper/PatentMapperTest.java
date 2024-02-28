package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.mapper.MapperHelper;
import bg.duosoft.ipas.core.mapper.patent.PatentMapper;
import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.core.model.patent.CDrawing;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;
import bg.duosoft.ipas.core.model.person.CAuthor;
import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class PatentMapperTest extends MapperTestBase {
    @Autowired
    private IpPatentRepository ipPatentRepository;


    @Autowired
    private PatentMapper patentMapper;

    private IpFilePK ipFilePKTechnicalData;

    private IpFilePK ipFilePKClaim;

    private IpFilePK ipFilePkForIpcClasses;

    private IpFilePK ipFilePkForDrawings;

    private IpFilePK ipFilePKApplicationData;

    IpFilePK ipFilePKAuthorData;

    IpFilePK ipFilePKOwnerData;

    IpFilePK ipFilePKServicePersonData;

    IpFilePK ipFilePKReprsData;

    IpFilePK ipFilePKMainOwnerData;

    IpFilePK ipFilePKPrioritiesData;

    private IpFilePK ipFilePKMainData;

    @Before
    public void init() {
        ipFilePKTechnicalData = new IpFilePK("BG", "P", 2017, 112451);
        ipFilePKMainData= new IpFilePK("BG", "P", 2015, 112121);
        ipFilePKApplicationData= new IpFilePK("BG", "P", 2003, 108001);
        ipFilePkForDrawings = new IpFilePK("BG", "P", 2014, 111675);
        ipFilePKClaim = new IpFilePK("BG", "P", 2004, 108514);
        ipFilePkForIpcClasses = new IpFilePK("BG", "P", 2018, 112710);
        ipFilePKAuthorData=new IpFilePK("BG", "P", 2018, 112683);
        ipFilePKOwnerData=new IpFilePK("BG", "P", 2018, 112809);
        ipFilePKServicePersonData= new IpFilePK("BG","P",2018,112693);
        ipFilePKReprsData= new IpFilePK("BG","P",2018,112668);
        ipFilePKMainOwnerData= new IpFilePK("BG","P",2018,112675);
        ipFilePKPrioritiesData= new IpFilePK("BG","P",2018,112701);
    }

    // Test conversion form IpPatent to CPatent.
    @Test
    @Transactional
    public void transformtoCoreTestCMainData(){
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, true);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertNotNull(originalPatent.getIpPatentDrawings());
        assertTrue(originalPatent.getIpPatentDrawings().size()>0);

        assertNotNull(cPatent);
        assertTrue(cPatent.getPatentContainsDrawingList());
        assertTrue(cPatent.getIndReadDrawingList());
        assertNotNull(cPatent.getRowVersion());
        assertEquals(cPatent.getRowVersion().longValue(),originalPatent.getRowVersion().longValue());
        assertEquals(MapperHelper.getTextAsBoolean(originalPatent.getIndRegistered()),cPatent.getFile().getRegistrationData().getIndRegistered());
    }

    @Test
    @Transactional
    public void transformtoCoreTestCPctApplicationData(){

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKApplicationData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertNotNull(cPatent);
        assertNotNull(originalPatent.getPctPhase());
        assertNotNull(cPatent.getPctApplicationData().getPctPhase());
        assertEquals(cPatent.getPctApplicationData().getPctPhase().longValue(),originalPatent.getPctPhase().longValue());
        assertNotNull(originalPatent.getPctApplId());
        assertNotNull(cPatent.getPctApplicationData().getPctApplicationId());
        assertEquals(cPatent.getPctApplicationData().getPctApplicationId(),originalPatent.getPctApplId());
        assertNotNull(originalPatent.getPctApplDate());
        assertNotNull(cPatent.getPctApplicationData().getPctApplicationDate());
        assertEquals(cPatent.getPctApplicationData().getPctApplicationDate(),originalPatent.getPctApplDate());
        assertNotNull(originalPatent.getPctPublCountry());
        assertNotNull(cPatent.getPctApplicationData().getPctPublicationCountryCode());
        assertEquals(cPatent.getPctApplicationData().getPctPublicationCountryCode(),originalPatent.getPctPublCountry());
        assertNotNull(originalPatent.getPctPublDate());
        assertNotNull(cPatent.getPctApplicationData().getPctPublicationDate());
        assertEquals(cPatent.getPctApplicationData().getPctPublicationDate(),originalPatent.getPctPublDate());
        assertNotNull(originalPatent.getPctPublTyp());
        assertNotNull(cPatent.getPctApplicationData().getPctPublicationType());
        assertEquals(cPatent.getPctApplicationData().getPctPublicationType(),originalPatent.getPctPublTyp());
        assertNotNull(originalPatent.getPctPublId());
        assertNotNull(cPatent.getPctApplicationData().getPctPublicationId());
        assertEquals(cPatent.getPctApplicationData().getPctPublicationId(),originalPatent.getPctPublId());
    }

    @Test
    @Transactional
    public void transformtoCoreTestCTechnicalData() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKTechnicalData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertNotNull(cPatent);
        assertNotNull(cPatent.getTechnicalData());
        assertTrue(!cPatent.getTechnicalData().getMainAbstract().isEmpty());
        assertTrue(!cPatent.getTechnicalData().getEnglishAbstract().isEmpty());
        assertEquals(cPatent.getTechnicalData().getTitle(), originalPatent.getTitle());
        assertEquals(cPatent.getTechnicalData().getEnglishTitle(), originalPatent.getEnglishTitle());
        assertTrue(!cPatent.getTechnicalData().getMainAbstract().isEmpty());
        assertTrue(!cPatent.getTechnicalData().getEnglishAbstract().isEmpty());

        List<IpPatentSummary> ipPatentSummaries = originalPatent.getIpPatentSummaries();
        String ipPatentMainAbstract = "";
        String ipPatentEnglishAbstract = "";
        for (IpPatentSummary ipPatentSummary :ipPatentSummaries) {
            if (ipPatentSummary.getPk().getLanguageCode().equals("MX")){
                ipPatentMainAbstract = ipPatentSummary.getSummary();
            }
            if (ipPatentSummary.getPk().getLanguageCode().equals("US")){
                ipPatentEnglishAbstract = ipPatentSummary.getSummary();
            }
        }
        assertEquals(ipPatentMainAbstract, cPatent.getTechnicalData().getMainAbstract());
        assertEquals(ipPatentEnglishAbstract, cPatent.getTechnicalData().getEnglishAbstract());

        assertEquals(cPatent.getTechnicalData().getLastClaimsPageRef(), originalPatent.getLastClaimsPageRef());
        assertEquals(cPatent.getTechnicalData().getLastDescriptionPageRef(), originalPatent.getLastDescriptionPageRef());

        // TODO noveltyDate

        assertEquals(cPatent.getTechnicalData().getHasIpc(), originalPatent.getIpPatentIpcClasses().isEmpty());
        assertEquals(cPatent.getTechnicalData().getHasCpc(), originalPatent.getIpPatentClaims().isEmpty());
    }

    @Test
    @Transactional
    public void transformtoCoreTestCIpcClassList() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePkForIpcClasses);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertEquals(cPatent.getTechnicalData().getHasIpc(), originalPatent.getIpPatentIpcClasses().isEmpty());

        assertEquals(cPatent.getTechnicalData().getIpcClassList().size(), 4);
        assertEquals(cPatent.getTechnicalData().getIpcClassList().size(), originalPatent.getIpPatentIpcClasses().size());
        for (int i = 0; i < originalPatent.getIpPatentIpcClasses().size(); i++) {
            CPatentIpcClass cPatentIpcClass = cPatent.getTechnicalData().getIpcClassList().get(i);
            IpPatentIpcClasses ipPatentIpcClasses = originalPatent.getIpPatentIpcClasses().get(i);

            assertEquals(cPatentIpcClass.getIpcEdition(), ipPatentIpcClasses.getPk().getIpcEditionCode());
            assertEquals(cPatentIpcClass.getIpcSection(), ipPatentIpcClasses.getPk().getIpcSectionCode());
            assertEquals(cPatentIpcClass.getIpcClass(), ipPatentIpcClasses.getPk().getIpcClassCode());
            assertEquals(cPatentIpcClass.getIpcSubclass(), ipPatentIpcClasses.getPk().getIpcSubclassCode());
            assertEquals(cPatentIpcClass.getIpcGroup(), ipPatentIpcClasses.getPk().getIpcGroupCode());
            assertEquals(cPatentIpcClass.getIpcQualification(), ipPatentIpcClasses.getPk().getIpcQualificationCode());
            assertEquals(cPatentIpcClass.getIpcSymbolPosition(), ipPatentIpcClasses.getIpcSymbolPosition());
            assertEquals(cPatentIpcClass.getIpcVersionCalculated(), ipPatentIpcClasses.getCfClassIpc().getIpcLatestVersion());
            assertEquals(cPatentIpcClass.getIpcEditionOriginal(), ipPatentIpcClasses.getCfClassIpc().getPk().getIpcEditionCode());
            assertEquals(cPatentIpcClass.getIpcSymbolDescription(), ipPatentIpcClasses.getCfClassIpc().getIpcName());
        }
    }

    @Test
    @Transactional
    public void transformtoCoreTestCClaimList() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertTrue(!cPatent.getTechnicalData().getClaimList().isEmpty());
        assertEquals(cPatent.getTechnicalData().getClaimList().size(), 1);
        assertEquals(cPatent.getTechnicalData().getClaimList().size() * 2, originalPatent.getIpPatentClaims().size());

        List<CClaim> claimList =new ArrayList<>();

        for (int i = 0; i < cPatent.getTechnicalData().getClaimList().size(); i++){
            CClaim cClaimMX=cPatent.getTechnicalData().getClaimList().get(i);
            CClaim cClaimUS=cPatent.getTechnicalData().getClaimList().get(i);
            claimList.add(cClaimUS);
            claimList.add(cClaimMX);


        }

        for (int i = 0; i < originalPatent.getIpPatentClaims().size(); i++) {
            IpPatentClaims ipPatentClaims = originalPatent.getIpPatentClaims().get(i);

            assertEquals(claimList.get(i).getClaimNbr().longValue(), ipPatentClaims.getPk().getClaimNbr().longValue());

            if (ipPatentClaims.getPk().getLanguageCode().equals("MX")){
                String ipPatentClaimDescription = ipPatentClaims.getClaimDescription();

                assertEquals(claimList.get(i).getClaimDescription(), ipPatentClaimDescription);
            }
            if (ipPatentClaims.getPk().getLanguageCode().equals("US")){
                String ipPatentClaimEnglishDescription = ipPatentClaims.getClaimDescription();

                assertEquals(claimList.get(i).getClaimEnglishDescription(), ipPatentClaimEnglishDescription);
            }
        }
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentClaims() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));

        assertNotNull(originalPatent.getIpPatentClaims());
        assertNotNull(comparePatent);
        assertNotNull(comparePatent.getIpPatentClaims());

        assertEquals(originalPatent.getIpPatentClaims().size(),comparePatent.getIpPatentClaims().size());

        for (int i = 0; i < originalPatent.getIpPatentClaims().size(); i++) {
                IpPatentClaims ipPatentClaims=originalPatent.getIpPatentClaims().get(i);
                assertTrue(comparePatent.getIpPatentClaims().contains(ipPatentClaims));
        }

    }


    @Test
    @Transactional
    public void transformtoCoreTestCDrawingList() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePkForDrawings);
        CPatent cPatent = patentMapper.toCore(originalPatent, true);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertNotNull(cPatent.getTechnicalData().getDrawingList());
        assertEquals(cPatent.getTechnicalData().getDrawingList().size(), 6);
        assertEquals(cPatent.getTechnicalData().getDrawingList().size(), originalPatent.getIpPatentDrawings().size());
        for (int i = 0; i < originalPatent.getIpPatentDrawings().size(); i++) {
            CDrawing cDrawing = cPatent.getTechnicalData().getDrawingList().get(i);
            IpPatentDrawings ipPatentDrawing = originalPatent.getIpPatentDrawings().get(i);

            assertEquals(cDrawing.getDrawingNbr().longValue(), ipPatentDrawing.getPk().getDrawingNbr().longValue());
            assertEquals(MapperHelper.logoTypeToFormatWCode(cDrawing.getDrawingType()), ipPatentDrawing.getImageFormatWCode());
            assertArrayEquals(cDrawing.getDrawingData(), ipPatentDrawing.getDrawingData());
            assertEquals(cDrawing.getBase64EncodedDrawingData(), Base64.getEncoder().encodeToString(ipPatentDrawing.getDrawingData()));
        }

        CPatent cPatentWithoutDrolings = patentMapper.toCore(originalPatent, false);
        List<CDrawing> drawingListWithoutContent = cPatentWithoutDrolings.getTechnicalData().getDrawingList();
        for (CDrawing cDrawing : drawingListWithoutContent) {
            byte[] drawingData = cDrawing.getDrawingData();
            assertTrue(null == drawingData || drawingData.length == 0);
        }
    }

    // TODO Test for CFile

    @Test
    @Transactional
    public void transformtoCoreTestCAuthorshipData() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKAuthorData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertNotNull(originalPatent.getIpPatentInventors());
        assertNotNull(cPatent);
        assertNotNull(cPatent.getAuthorshipData());
//        assertTrue(cPatent.getAuthorshipData().getIndOwnerSameAuthor());
        assertNotNull(cPatent.getAuthorshipData().getAuthorList());
        assertEquals(originalPatent.getIpPatentInventors().size(), cPatent.getAuthorshipData().getAuthorList().size());

        for (int i = 0; i < originalPatent.getIpPatentInventors().size(); i++) {
            CAuthor cAuthor = cPatent.getAuthorshipData().getAuthorList().get(i);
            IpPatentInventors ipPatentInventors = originalPatent.getIpPatentInventors().get(i);

            assertEquals(ipPatentInventors.getSeqNbr().longValue(), cAuthor.getAuthorSeq().longValue());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getPk().getPersonNbr(), cAuthor.getPerson().getPersonNbr());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getPk().getAddrNbr(), cAuthor.getPerson().getAddressNbr());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getPersonName(), cAuthor.getPerson().getPersonName());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getNationalityCountryCode(), cAuthor.getPerson().getNationalityCountryCode());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getIndCompany());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getLegalNature(), cAuthor.getPerson().getLegalNature());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getLegalIdType());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getLegalIdNbr());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getIndividualIdType());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getIndividualIdNbr());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getAgentCode());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getStateName(), cAuthor.getPerson().getStateName());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getCityName(), cAuthor.getPerson().getCityName());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getAddrZone(), cAuthor.getPerson().getAddressZone());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getAddrStreet(), cAuthor.getPerson().getAddressStreet());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getZipcode(), cAuthor.getPerson().getZipCode());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getEmail(), cAuthor.getPerson().getEmail());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getTelephone(), cAuthor.getPerson().getTelephone());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getCompanyRegisterDate(), cAuthor.getPerson().getCompanyRegisterRegistrationDate());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getCompanyRegisterNbr(), cAuthor.getPerson().getCompanyRegisterRegistrationNbr());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getResidenceCountry().getCountryCode(), cAuthor.getPerson().getResidenceCountryCode());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getStateCode(), cAuthor.getPerson().getStateCode());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getCityCode(), cAuthor.getPerson().getCityCode());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getPersonGroupCode());
//            assertEquals(ipPatentInventors.getIpPersonAddresses().get, cAuthor.getPerson().getPersonGroupName());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getPersonNameLang2(), cAuthor.getPerson().getPersonNameInOtherLang());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getIpPerson().getLegalNatureLang2(), cAuthor.getPerson().getLegalNatureInOtherLang());
            assertEquals(ipPatentInventors.getIpPersonAddresses().getAddrStreetLang2(), cAuthor.getPerson().getAddressStreetInOtherLang());
        }
    }

    @Test
    @Transactional
    public void transformtoCoreTestCAuthorshipData2() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKAuthorData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        assertNotNull(cPatent);
        assertNotNull(originalPatent.getIpPatentInventors());
        assertNotNull(cPatent.getAuthorshipData());
        assertNotNull(cPatent.getAuthorshipData().getAuthorList());
        assertTrue(originalPatent.getIpPatentInventors().size()==cPatent.getAuthorshipData().getAuthorList().size());

        //assertTrue(cPatent.getAuthorshipData().getIndOwnerSameAuthor());
        assertFalse(cPatent.getAuthorshipData().getIndOwnerSameAuthor());

        for (int i = 0; i < originalPatent.getIpPatentInventors().size(); i++) {
            IpPatentInventors originalIpPatentInventors = originalPatent.getIpPatentInventors().get(i);
            IpPersonAddresses originalAddress=originalIpPatentInventors.getIpPersonAddresses();
            IpPerson originalIpPerson= originalAddress.getIpPerson();


            CPerson cPerson=cPatent.getAuthorshipData().getAuthorList().get(i).getPerson();
            CAuthor cAuthor=cPatent.getAuthorshipData().getAuthorList().get(i);

            assertEquals(cAuthor.getAuthorSeq().longValue(),originalIpPatentInventors.getSeqNbr().longValue());
            assertEquals(originalIpPerson.getPersonName(),cPerson.getPersonName());
            assertEquals(originalIpPerson.getNationalityCountryCode(),cPerson.getNationalityCountryCode());
            assertEquals(originalIpPerson.getLegalNature(),cPerson.getLegalNature());
            assertEquals(originalAddress.getAddrStreet(),cPerson.getAddressStreet());
            assertEquals(originalAddress.getZipcode(),cPerson.getZipCode());
            assertEquals(originalAddress.getStateName(),cPerson.getStateName());
            assertEquals(originalAddress.getStateCode(),cPerson.getStateCode());
            assertEquals(originalAddress.getAddrZone(),cPerson.getAddressZone());
            assertEquals(originalIpPerson.getEmail(),cPerson.getEmail());
            assertEquals(originalIpPerson.getTelephone(),cPerson.getTelephone());
            assertEquals(originalIpPerson.getCompanyRegisterDate(),cPerson.getCompanyRegisterRegistrationDate());
//            assertEquals(originalIpPerson.getCompanyRegisterNbr(),cPerson.getCompanyRegisterRegistrationNbr());

            assertEquals(originalAddress.getResidenceCountry().getCountryCode(),cPerson.getResidenceCountryCode());
            assertEquals(originalAddress.getCityCode(),cPerson.getCityCode());
            assertEquals(originalAddress.getCityName(),cPerson.getCityName());
            assertEquals(originalIpPerson.getPersonNameLang2(),cPerson.getPersonNameInOtherLang());
            assertEquals(originalIpPerson.getLegalNatureLang2(),cPerson.getLegalNatureInOtherLang());
            assertEquals(originalAddress.getPk().getPersonNbr(),cPerson.getPersonNbr());
            assertEquals(originalAddress.getPk().getAddrNbr(),cPerson.getAddressNbr());
            assertEquals(originalAddress.getAddrStreetLang2(),cPerson.getAddressStreetInOtherLang());
        }
    }


    // Test conversion from IpPatent to CPatent and then to IpPatent

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpPatent() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));

        assertEquals(ipPatent.getRowVersion(), originalPatent.getRowVersion());
        assertNotNull(ipPatent.getPk());
//        assertEquals(ipPatent.getPk().getFileSeq(), originalPatent.getPk().getFileSeq());
//        assertEquals(ipPatent.getPk().getFileTyp(), originalPatent.getPk().getFileTyp());
//        assertEquals(ipPatent.getPk().getFileSer(), originalPatent.getPk().getFileSer());
//        assertEquals(ipPatent.getPk().getFileNbr(), originalPatent.getPk().getFileNbr());
//        assertEquals(ipPatent.getReceptionDate(), originalPatent.getReceptionDate()); // TODO
//        assertEquals(ipPatent.getFilingDate(), originalPatent.getFilingDate()); // TODO BUG: filingDate is part of filingData.filingDate, but it is not convert
//        assertEquals(ipPatent.getFirstPriorityDate(), originalPatent.getFirstPriorityDate()); // TODO
//        assertEquals(ipPatent.getExhibitionDate(), originalPatent.getExhibitionDate()); // TODO
        assertEquals(ipPatent.getSpecialPublApplDate(), originalPatent.getSpecialPublApplDate());
//        assertEquals(ipPatent.getPublicationDate(), originalPatent.getPublicationDate()); // TODO
        assertEquals(ipPatent.getJournalCode(), originalPatent.getJournalCode());
        assertEquals(ipPatent.getArt2TDocNbr(), originalPatent.getArt2TDocNbr());
        assertEquals(ipPatent.getArt2TDate(), originalPatent.getArt2TDate());
        assertEquals(ipPatent.getArt10TDocNbr(), originalPatent.getArt10TDocNbr());
        assertEquals(ipPatent.getArt10TDate(), originalPatent.getArt10TDate());
//        assertEquals(ipPatent.getRegistrationNbr(), originalPatent.getRegistrationNbr());
//        assertEquals(ipPatent.getRegistrationDate(), originalPatent.getRegistrationDate());
//        assertEquals(ipPatent.getIndRegistered(), originalPatent.getIndRegistered());
//        assertEquals(ipPatent.getEntitlementDate(), originalPatent.getEntitlementDate());
//        assertEquals(ipPatent.getExpirationDate(), originalPatent.getExpirationDate());
        assertEquals(ipPatent.getTitle(), originalPatent.getTitle());
        assertEquals(ipPatent.getIndOwnerSameasInventor(), originalPatent.getIndOwnerSameasInventor());
        assertEquals(ipPatent.getPoaDocOri(), originalPatent.getPoaDocOri());
        assertEquals(ipPatent.getPoaLogTyp(), originalPatent.getPoaLogTyp());
        assertEquals(ipPatent.getPoaDocSer(), originalPatent.getPoaDocSer());
        assertEquals(ipPatent.getPoaDocNbr(), originalPatent.getPoaDocNbr());
//        assertEquals(ipPatent.getCaptureDate(), originalPatent.getCaptureDate());
        assertEquals(ipPatent.getPctApplId(), originalPatent.getPctApplId());
        assertEquals(ipPatent.getPctApplDate(), originalPatent.getPctApplDate());
        assertEquals(ipPatent.getPctPublCountry(), originalPatent.getPctPublCountry());
        assertEquals(ipPatent.getPctPublId(), originalPatent.getPctPublId());
        assertEquals(ipPatent.getPctPublTyp(), originalPatent.getPctPublTyp());
        assertEquals(ipPatent.getPctPublDate(), originalPatent.getPctPublDate());
        assertEquals(ipPatent.getPctPhase(), originalPatent.getPctPhase());
        assertEquals(ipPatent.getEnglishTitle(), originalPatent.getEnglishTitle());
//        assertEquals(ipPatent.getPublicationNotes(), originalPatent.getPublicationNotes());
        assertEquals(ipPatent.getLastDescriptionPageRef(), originalPatent.getLastDescriptionPageRef());
        assertEquals(ipPatent.getLastClaimsPageRef(), originalPatent.getLastClaimsPageRef());
        assertEquals(ipPatent.getSpecialPublDate(), originalPatent.getSpecialPublDate());
//        assertEquals(ipPatent.getExhibitionNotes(), originalPatent.getExhibitionNotes());
//        assertEquals(ipPatent.getRegistrationTyp(), originalPatent.getRegistrationTyp());
        assertEquals(ipPatent.getRegistrationSer(), originalPatent.getRegistrationSer());
//        assertEquals(ipPatent.getRegistrationDup(), originalPatent.getRegistrationDup());
//        assertEquals(ipPatent.getExamIpcUsed(), originalPatent.getExamIpcUsed());
//        assertEquals(ipPatent.getExamKeywordsUsed(), originalPatent.getExamKeywordsUsed());
//        assertEquals(ipPatent.getIndExamNovelty(), originalPatent.getIndExamNovelty());
//        assertEquals(ipPatent.getIndExamInventive(), originalPatent.getIndExamInventive());
//        assertEquals(ipPatent.getIndExamIndustrial(), originalPatent.getIndExamIndustrial());
//        assertEquals(ipPatent.getExamResult(), originalPatent.getExamResult());
//        assertEquals(ipPatent.getIndManualInterpretation(), originalPatent.getIndManualInterpretation());
        assertEquals(ipPatent.getRegionalApplDate(), originalPatent.getRegionalApplDate());
//        assertEquals(ipPatent.getRegionalApplId(), originalPatent.getRegionalApplId());
//        assertEquals(ipPatent.getRegionalPublCountry(), originalPatent.getRegionalPublCountry());
        assertEquals(ipPatent.getRegionalPublDate(), originalPatent.getRegionalPublDate());
//        assertEquals(ipPatent.getRegionalPublId(), originalPatent.getRegionalPublId());
//        assertEquals(ipPatent.getRegionalPublTyp(), originalPatent.getRegionalPublTyp());
//        assertEquals(ipPatent.getNovelty1Date(), originalPatent.getNovelty1Date());
//        assertEquals(ipPatent.getNovelty2Date(), originalPatent.getNovelty2Date());
        assertEquals(ipPatent.getPublicationNbr(), originalPatent.getPublicationNbr());
        assertEquals(ipPatent.getPublicationSer(), originalPatent.getPublicationSer());
        assertEquals(ipPatent.getPublicationTyp(), originalPatent.getPublicationTyp());
        assertEquals(cPatent.getFile().getRegistrationData().getIndRegistered(),MapperHelper.getTextAsBoolean(ipPatent.getIndRegistered()));
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileMainData() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));
        checkIpFile(originalPatent.getFile(), ipPatent.getFile());
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileIpDocFileList() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));

        assertNotEquals(ipPatent.getFile(), originalPatent.getFile());

        assertNotNull(originalPatent.getFile().getIpDocFilesCollection());
 /*       assertNotNull(ipPatent.getFile().getIpDocFilesCollection());
        assertEquals(ipPatent.getFile().getIpDocFilesCollection().size(), originalPatent.getFile().getIpDocFilesCollection().size());
        for (int i = 0; i < ipPatent.getFile().getIpDocFilesCollection().size(); i++) {
            IpDocFiles ipDocFiles = ipPatent.getFile().getIpDocFilesCollection().get(i);
            IpDocFiles originalIpDocFiles = originalPatent.getFile().getIpDocFilesCollection().get(i);

            assertEquals(ipDocFiles.getRowVersion(), originalIpDocFiles.getRowVersion());
            assertEquals(ipDocFiles.getPk().getDocOri(), originalIpDocFiles.getPk().getDocOri());
            assertEquals(ipDocFiles.getPk().getDocLog(), originalIpDocFiles.getPk().getDocLog());
            assertEquals(ipDocFiles.getPk().getDocSer(), originalIpDocFiles.getPk().getDocSer());
            assertEquals(ipDocFiles.getPk().getDocNbr(), originalIpDocFiles.getPk().getDocNbr());
            assertEquals(ipDocFiles.getPk().getFileSeq(), originalIpDocFiles.getPk().getFileSeq());
            assertEquals(ipDocFiles.getPk().getFileTyp(), originalIpDocFiles.getPk().getFileTyp());
            assertEquals(ipDocFiles.getPk().getFileSer(), originalIpDocFiles.getPk().getFileSer());
            assertEquals(ipDocFiles.getPk().getFileNbr(), originalIpDocFiles.getPk().getFileNbr());
            assertEquals(ipDocFiles.getPriorExpirationDate(), originalIpDocFiles.getPriorExpirationDate());
        }
        */
    } // TODO

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileIpDoc() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));

        assertNotEquals(ipPatent.getFile(), originalPatent.getFile());

        assertNotNull(originalPatent.getFile().getIpDoc());
        assertNotNull(ipPatent.getFile().getIpDoc());
//        assertEquals(ipPatent.getFile().getIpDoc().getRowVersion(), originalPatent.getFile().getIpDoc().getRowVersion());
//        assertEquals(ipPatent.getFile().getIpDoc().getPk().getDocOri(), originalPatent.getFile().getIpDoc().getPk().getDocOri());
//        assertEquals(ipPatent.getFile().getIpDoc().getPk().getDocLog(), originalPatent.getFile().getIpDoc().getPk().getDocLog());
//        assertEquals(ipPatent.getFile().getIpDoc().getPk().getDocSer(), originalPatent.getFile().getIpDoc().getPk().getDocSer());
//        assertEquals(ipPatent.getFile().getIpDoc().getPk().getDocNbr(), originalPatent.getFile().getIpDoc().getPk().getDocNbr());
        assertEquals(ipPatent.getFile().getIpDoc().getFilingDate(), originalPatent.getFile().getIpDoc().getFilingDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getReceptionWcode(), originalPatent.getFile().getIpDoc().getReceptionWcode());
        assertEquals(ipPatent.getFile().getIpDoc().getExternalOfficeCode(), originalPatent.getFile().getIpDoc().getExternalOfficeCode());
        assertEquals(ipPatent.getFile().getIpDoc().getExternalOfficeFilingDate(), originalPatent.getFile().getIpDoc().getExternalOfficeFilingDate());
        assertEquals(ipPatent.getFile().getIpDoc().getExternalSystemId(), originalPatent.getFile().getIpDoc().getExternalSystemId());
        assertEquals(ipPatent.getFile().getIpDoc().getIndNotAllFilesCapturedYet(), originalPatent.getFile().getIpDoc().getIndNotAllFilesCapturedYet());
        assertEquals(ipPatent.getFile().getIpDoc().getNotes(), originalPatent.getFile().getIpDoc().getNotes());
//        assertEquals(ipPatent.getFile().getIpDoc().getApplSubtyp(), originalPatent.getFile().getIpDoc().getApplSubtyp());
        assertEquals(ipPatent.getFile().getIpDoc().getAuxil1Nbr(), originalPatent.getFile().getIpDoc().getAuxil1Nbr());
        assertEquals(ipPatent.getFile().getIpDoc().getReceptionDate(), originalPatent.getFile().getIpDoc().getReceptionDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getDocSeqTyp(), originalPatent.getFile().getIpDoc().getDocSeqTyp());
//        assertEquals(ipPatent.getFile().getIpDoc().getDocSeqNbr(), originalPatent.getFile().getIpDoc().getDocSeqNbr());
//        assertEquals(ipPatent.getFile().getIpDoc().getDocSeqSeries(), originalPatent.getFile().getIpDoc().getDocSeqSeries());
        assertEquals(ipPatent.getFile().getIpDoc().getReceptionUserId(), originalPatent.getFile().getIpDoc().getReceptionUserId());
        assertEquals(ipPatent.getFile().getIpDoc().getIndFaxReception(), originalPatent.getFile().getIpDoc().getIndFaxReception());
        assertEquals(ipPatent.getFile().getIpDoc().getFaxRecepDate(), originalPatent.getFile().getIpDoc().getFaxRecepDate());
        assertEquals(ipPatent.getFile().getIpDoc().getAckOffidocOri(), originalPatent.getFile().getIpDoc().getAckOffidocOri());
        assertEquals(ipPatent.getFile().getIpDoc().getAckOffidocSer(), originalPatent.getFile().getIpDoc().getAckOffidocSer());
        assertEquals(ipPatent.getFile().getIpDoc().getAckOffidocNbr(), originalPatent.getFile().getIpDoc().getAckOffidocNbr());
        assertEquals(ipPatent.getFile().getIpDoc().getApplDocOri(), originalPatent.getFile().getIpDoc().getApplDocOri());
        assertEquals(ipPatent.getFile().getIpDoc().getApplLogTyp(), originalPatent.getFile().getIpDoc().getApplLogTyp());
        assertEquals(ipPatent.getFile().getIpDoc().getApplDocSer(), originalPatent.getFile().getIpDoc().getApplDocSer());
        assertEquals(ipPatent.getFile().getIpDoc().getApplDocNbr(), originalPatent.getFile().getIpDoc().getApplDocNbr());
        assertEquals(ipPatent.getFile().getIpDoc().getIncompleteApplDocOri(), originalPatent.getFile().getIpDoc().getIncompleteApplDocOri());
        assertEquals(ipPatent.getFile().getIpDoc().getIncompleteApplLogTyp(), originalPatent.getFile().getIpDoc().getIncompleteApplLogTyp());
        assertEquals(ipPatent.getFile().getIpDoc().getIncompleteApplDocSer(), originalPatent.getFile().getIpDoc().getIncompleteApplDocSer());
        assertEquals(ipPatent.getFile().getIpDoc().getIncompleteApplDocNbr(), originalPatent.getFile().getIpDoc().getIncompleteApplDocNbr());
        assertEquals(ipPatent.getFile().getIpDoc().getIndOriginalFax(), originalPatent.getFile().getIpDoc().getIndOriginalFax());
        assertEquals(ipPatent.getFile().getIpDoc().getRespondedDocOri(), originalPatent.getFile().getIpDoc().getRespondedDocOri());
        assertEquals(ipPatent.getFile().getIpDoc().getRespondedLogTyp(), originalPatent.getFile().getIpDoc().getRespondedLogTyp());
        assertEquals(ipPatent.getFile().getIpDoc().getRespondedDocSer(), originalPatent.getFile().getIpDoc().getRespondedDocSer());
        assertEquals(ipPatent.getFile().getIpDoc().getRespondedDocNbr(), originalPatent.getFile().getIpDoc().getRespondedDocNbr());
        assertEquals(ipPatent.getFile().getIpDoc().getMailNotifDate(), originalPatent.getFile().getIpDoc().getMailNotifDate());
        assertEquals(ipPatent.getFile().getIpDoc().getIndResponseReq(), originalPatent.getFile().getIpDoc().getIndResponseReq());
        assertEquals(ipPatent.getFile().getIpDoc().getIndSystem(), originalPatent.getFile().getIpDoc().getIndSystem());
        assertEquals(ipPatent.getFile().getIpDoc().getOffidocOri(), originalPatent.getFile().getIpDoc().getOffidocOri());
        assertEquals(ipPatent.getFile().getIpDoc().getOffidocSer(), originalPatent.getFile().getIpDoc().getOffidocSer());
        assertEquals(ipPatent.getFile().getIpDoc().getOffidocNbr(), originalPatent.getFile().getIpDoc().getOffidocNbr());
        assertEquals(ipPatent.getFile().getIpDoc().getDocDescription(), originalPatent.getFile().getIpDoc().getDocDescription());
        assertEquals(ipPatent.getFile().getIpDoc().getDocRef(), originalPatent.getFile().getIpDoc().getDocRef());
        assertEquals(ipPatent.getFile().getIpDoc().getOfficeDivisionCode(), originalPatent.getFile().getIpDoc().getOfficeDivisionCode());
        assertEquals(ipPatent.getFile().getIpDoc().getOfficeDepartmentCode(), originalPatent.getFile().getIpDoc().getOfficeDepartmentCode());
        assertEquals(ipPatent.getFile().getIpDoc().getOfficeSectionCode(), originalPatent.getFile().getIpDoc().getOfficeSectionCode());
        assertEquals(ipPatent.getFile().getIpDoc().getOrigRecepDate(), originalPatent.getFile().getIpDoc().getOrigRecepDate());
        assertEquals(ipPatent.getFile().getIpDoc().getIndSpecificEdoc(), originalPatent.getFile().getIpDoc().getIndSpecificEdoc());
        assertEquals(ipPatent.getFile().getIpDoc().getIndInterfaceEdoc(), originalPatent.getFile().getIpDoc().getIndInterfaceEdoc());
        assertEquals(ipPatent.getFile().getIpDoc().getCreatedEfolderId(), originalPatent.getFile().getIpDoc().getCreatedEfolderId());
        assertEquals(ipPatent.getFile().getIpDoc().getEdocId(), originalPatent.getFile().getIpDoc().getEdocId());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeTyp1(), originalPatent.getFile().getIpDoc().getDataCodeTyp1());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeId1(), originalPatent.getFile().getIpDoc().getDataCodeId1());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeTyp2(), originalPatent.getFile().getIpDoc().getDataCodeTyp2());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeId2(), originalPatent.getFile().getIpDoc().getDataCodeId2());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeTyp3(), originalPatent.getFile().getIpDoc().getDataCodeTyp3());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeId3(), originalPatent.getFile().getIpDoc().getDataCodeId3());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeTyp4(), originalPatent.getFile().getIpDoc().getDataCodeTyp4());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeId4(), originalPatent.getFile().getIpDoc().getDataCodeId4());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeTyp5(), originalPatent.getFile().getIpDoc().getDataCodeTyp5());
        assertEquals(ipPatent.getFile().getIpDoc().getDataCodeId5(), originalPatent.getFile().getIpDoc().getDataCodeId5());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataText1(), originalPatent.getFile().getIpDoc().getDataText1());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataText2(), originalPatent.getFile().getIpDoc().getDataText2());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataText3(), originalPatent.getFile().getIpDoc().getDataText3());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataText4(), originalPatent.getFile().getIpDoc().getDataText4());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataText5(), originalPatent.getFile().getIpDoc().getDataText5());
        assertEquals(ipPatent.getFile().getIpDoc().getDataDate1(), originalPatent.getFile().getIpDoc().getDataDate1());
        assertEquals(ipPatent.getFile().getIpDoc().getDataDate2(), originalPatent.getFile().getIpDoc().getDataDate2());
        assertEquals(ipPatent.getFile().getIpDoc().getDataDate3(), originalPatent.getFile().getIpDoc().getDataDate3());
        assertEquals(ipPatent.getFile().getIpDoc().getDataDate4(), originalPatent.getFile().getIpDoc().getDataDate4());
        assertEquals(ipPatent.getFile().getIpDoc().getDataDate5(), originalPatent.getFile().getIpDoc().getDataDate5());
        assertEquals(ipPatent.getFile().getIpDoc().getDataNbr1(), originalPatent.getFile().getIpDoc().getDataNbr1());
        assertEquals(ipPatent.getFile().getIpDoc().getDataNbr2(), originalPatent.getFile().getIpDoc().getDataNbr2());
        assertEquals(ipPatent.getFile().getIpDoc().getDataNbr3(), originalPatent.getFile().getIpDoc().getDataNbr3());
        assertEquals(ipPatent.getFile().getIpDoc().getDataNbr4(), originalPatent.getFile().getIpDoc().getDataNbr4());
        assertEquals(ipPatent.getFile().getIpDoc().getDataNbr5(), originalPatent.getFile().getIpDoc().getDataNbr5());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataFlag1(), originalPatent.getFile().getIpDoc().getDataFlag1());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataFlag2(), originalPatent.getFile().getIpDoc().getDataFlag2());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataFlag3(), originalPatent.getFile().getIpDoc().getDataFlag3());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataFlag4(), originalPatent.getFile().getIpDoc().getDataFlag4());
//        assertEquals(ipPatent.getFile().getIpDoc().getDataFlag5(), originalPatent.getFile().getIpDoc().getDataFlag5());
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileIpDocIpDailyLog() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));

        assertNotEquals(ipPatent.getFile(), originalPatent.getFile());
        assertNotNull(originalPatent.getFile().getIpDoc());
        assertNotNull(originalPatent.getFile().getIpDoc().getIpDailyLog());
//        assertNotNull(ipPatent.getFile().getIpDoc().getIpDailyLog());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getRowVersion(), originalPatent.getFile().getIpDoc().getIpDailyLog().getRowVersion());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getPk().getDocOri(), originalPatent.getFile().getIpDoc().getIpDailyLog().getPk().getDocOri());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getPk().getDailyLogDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getPk().getDailyLogDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getPk().getDocLog(), originalPatent.getFile().getIpDoc().getIpDailyLog().getPk().getDocLog());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getFirstDocNbr(), originalPatent.getFile().getIpDoc().getIpDailyLog().getFirstDocNbr());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getLastDocNbr(), originalPatent.getFile().getIpDoc().getIpDailyLog().getLastDocNbr());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getIndOpen(), originalPatent.getFile().getIpDoc().getIpDailyLog().getIndOpen());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getIndClosed(), originalPatent.getFile().getIpDoc().getIpDailyLog().getIndClosed());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getAffectedFilesReadyDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getAffectedFilesReadyDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getLogoCaptureReadyDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getLogoCaptureReadyDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getDigitalizationReadyDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getDigitalizationReadyDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getFileCaptureReadyDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getFileCaptureReadyDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getViennaCodesReadyDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getViennaCodesReadyDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getArchivingDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getArchivingDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getUserdocCaptureReadyDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getUserdocCaptureReadyDate());
//        assertEquals(ipPatent.getFile().getIpDoc().getIpDailyLog().getCertificationReadyDate(), originalPatent.getFile().getIpDoc().getIpDailyLog().getCertificationReadyDate());
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileIpDocCfApplicationType() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));

        assertNotEquals(ipPatent.getFile(), originalPatent.getFile());

        assertNotNull(originalPatent.getFile().getIpDoc());
        assertNotNull(ipPatent.getFile().getIpDoc());
        assertNotNull(originalPatent.getFile().getIpDoc().getApplTyp());
//        assertNotNull(ipPatent.getFile().getIpDoc().getCfApplicationType());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getRowVersion(), originalPatent.getFile().getIpDoc().getCfApplicationType().getRowVersion());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getApplTyp(), originalPatent.getFile().getIpDoc().getCfApplicationType().getApplTyp());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getApplTypeName(), originalPatent.getFile().getIpDoc().getCfApplicationType().getApplTypeName());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getCertificateTitle(), originalPatent.getFile().getIpDoc().getCfApplicationType().getCertificateTitle());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getFileTyp(), originalPatent.getFile().getIpDoc().getCfApplicationType().getFileTyp());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getGenerateProcTyp(), originalPatent.getFile().getIpDoc().getCfApplicationType().getGenerateProcTyp());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getTableName(), originalPatent.getFile().getIpDoc().getCfApplicationType().getTableName());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getParisPriorityMonths(), originalPatent.getFile().getIpDoc().getCfApplicationType().getParisPriorityMonths());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getIndIpc(), originalPatent.getFile().getIpDoc().getCfApplicationType().getIndIpc());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getIndLocarno(), originalPatent.getFile().getIpDoc().getCfApplicationType().getIndLocarno());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getExpositionPriorityMonths(), originalPatent.getFile().getIpDoc().getCfApplicationType().getExpositionPriorityMonths());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getXmlDesigner(), originalPatent.getFile().getIpDoc().getCfApplicationType().getXmlDesigner());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getIndInactive(), originalPatent.getFile().getIpDoc().getCfApplicationType().getIndInactive());
//        assertEquals(ipPatent.getFile().getIpDoc().getCfApplicationType().getIndValidInStates(), originalPatent.getFile().getIpDoc().getCfApplicationType().getIndValidInStates());
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileIpFileRelationshipList1() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));

        checkFileRelations(originalPatent.getFile().getIpFileRelationships1(), ipPatent.getFile().getIpFileRelationships1());
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileIpFileRelationshipList2() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(ipPatent));

        checkFileRelations(originalPatent.getFile().getIpFileRelationships2(), ipPatent.getFile().getIpFileRelationships2());

    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpFileCfLaw() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKClaim);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent ipPatent = patentMapper.toEntity(cPatent, false);
        checkCfLaw(originalPatent, ipPatent);
    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpPatentInventors() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKAuthorData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));

        assertNotNull(originalPatent.getIpPatentInventors());

        assertNotNull(comparePatent );
        assertNotNull( comparePatent.getIpPatentInventors());

        assertEquals(comparePatent.getIpPatentInventors().size(), originalPatent.getIpPatentInventors().size());
        assertEquals(comparePatent.getIndOwnerSameasInventor(), originalPatent.getIndOwnerSameasInventor());

        for (int i = 0; i < originalPatent.getIpPatentInventors().size(); i++) {
            IpPatentInventors originalIpPatentInventors = originalPatent.getIpPatentInventors().get(i);
            IpPatentInventors compareIpPatentInventors = comparePatent.getIpPatentInventors().get(i);

            assertEquals(originalIpPatentInventors.getSeqNbr(),compareIpPatentInventors.getSeqNbr());
            assertEquals(originalIpPatentInventors.getPk().getAddrNbr(),compareIpPatentInventors.getPk().getAddrNbr());
            IpPersonAddresses originalAddress=originalIpPatentInventors.getIpPersonAddresses();
            IpPersonAddresses compareAddress=compareIpPatentInventors.getIpPersonAddresses();

            IpPerson originalIpPerson= originalAddress.getIpPerson();
            IpPerson compareIpPerson= compareAddress.getIpPerson();
//            assertEquals(originalIpPerson.getCompanyRegisterNbr(),compareIpPerson.getCompanyRegisterNbr());

            assertEquals(originalIpPerson.getPersonName(),compareIpPerson.getPersonName());
            assertEquals(originalIpPerson.getNationalityCountryCode(),compareIpPerson.getNationalityCountryCode());
            assertEquals(originalIpPerson.getLegalNature(),compareIpPerson.getLegalNature());

            assertEquals(originalAddress.getAddrStreet(),compareAddress.getAddrStreet());
            assertEquals(originalAddress.getZipcode(),compareAddress.getZipcode());
            assertEquals(originalAddress.getStateName(),compareAddress.getStateName());
            assertEquals(originalAddress.getStateCode(),compareAddress.getStateCode());
            assertEquals(originalAddress.getAddrZone(),compareAddress.getAddrZone());

            assertEquals(originalIpPerson.getEmail(),compareIpPerson.getEmail());
            assertEquals(originalIpPerson.getTelephone(),compareIpPerson.getTelephone());
            assertEquals(originalIpPerson.getCompanyRegisterDate(),compareIpPerson.getCompanyRegisterDate());

            assertEquals(originalAddress.getResidenceCountry().getCountryCode(),
                    compareAddress.getResidenceCountry().getCountryCode());
            assertEquals(originalAddress.getCityCode(),compareAddress.getCityCode());
            assertEquals(originalAddress.getCityName(),compareAddress.getCityName());

            assertEquals(originalIpPerson.getPersonNameLang2(),compareIpPerson.getPersonNameLang2());
            assertEquals(originalIpPerson.getLegalNatureLang2(),compareIpPerson.getLegalNatureLang2());

            assertEquals(originalAddress.getPk().getPersonNbr(),compareAddress.getPk().getPersonNbr());
            assertEquals(originalAddress.getPk().getAddrNbr(),compareAddress.getPk().getAddrNbr());

        }
    }


    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpPatentApplicationData(){

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKApplicationData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);

        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));

        assertNotNull(originalPatent.getPctPhase());
        assertNotNull(originalPatent.getPctApplId());
        assertNotNull(originalPatent.getPctApplDate());
        assertNotNull(originalPatent.getPctPublCountry());
        assertNotNull(originalPatent.getPctPublDate());
        assertNotNull(originalPatent.getPctPublTyp());
        assertNotNull(originalPatent.getPctPublId());

        assertNotNull(comparePatent.getPctPhase());
        assertNotNull(comparePatent.getPctApplId());
        assertNotNull(comparePatent.getPctApplDate());
        assertNotNull(comparePatent.getPctPublCountry());
        assertNotNull(comparePatent.getPctPublDate());
        assertNotNull(comparePatent.getPctPublTyp());
        assertNotNull(comparePatent.getPctPublId());

        assertEquals(comparePatent.getPctPhase(),originalPatent.getPctPhase());
        assertEquals(comparePatent.getPctApplId(),originalPatent.getPctApplId());
        assertEquals(comparePatent.getPctApplDate(),originalPatent.getPctApplDate());
        assertEquals(comparePatent.getPctPublCountry(),originalPatent.getPctPublCountry());
        assertEquals(comparePatent.getPctPublDate(),originalPatent.getPctPublDate());
        assertEquals(comparePatent.getPctPublTyp(),originalPatent.getPctPublTyp());
        assertEquals(comparePatent.getPctPublId(),originalPatent.getPctPublId());

    }




    @Test
    @Transactional
    public void transformtoCoreTestCFileCOwnershipDataCOwner() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKOwnerData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));
        checkTransformedCOwnershipData(originalPatent, cPatent.getFile());
    }


    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestIpPatentOwners() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKOwnerData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));

        assertNotNull(originalPatent.getOwners());

        assertNotNull(comparePatent);
        assertNotNull(comparePatent.getOwners());

        assertEquals(comparePatent.getOwners().size(), originalPatent.getOwners().size());

        AtomicInteger orderNbr = new AtomicInteger(0);
        if (originalPatent.getOwners() != null) {
            Optional<Integer> maxExistingOrderNbr = originalPatent.getOwners().stream().filter(o -> o.getOrderNbr() != null).sorted(Comparator.comparingInt(IpPatentOwners::getOrderNbr).reversed()).map(o -> o.getOrderNbr()).findFirst();
            maxExistingOrderNbr.ifPresent(o -> orderNbr.set(o));
            originalPatent.getOwners().stream().filter(o -> o.getOrderNbr() == null).forEach(o -> o.setOrderNbr(orderNbr.incrementAndGet()));
        }
        compareIntellectualPropertyEntityOwners(originalPatent, comparePatent);
    }




    @Test
    @Transactional
    public void transformtoCoreTestCFileServicePerson() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKServicePersonData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        checkTransformedServicePerson(originalPatent, cPatent.getFile());

    }

    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestServicePerson() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKServicePersonData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));

        assertNotNull(originalPatent.getServicePerson());
        assertNotNull(comparePatent);
        assertNotNull(comparePatent.getServicePerson());

        IpPersonAddresses originalAddress=originalPatent.getServicePerson();
        IpPersonAddresses compareAddress=comparePatent.getServicePerson();

        compareAddressAndPersonDataOrigin(originalAddress,compareAddress);

    }


    @Test
    @Transactional
    public void transformtoCoreTestCFileCRepresentationDataCRepresentative() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKReprsData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));
        checkTransformedRepresentationData(originalPatent, cPatent.getFile());
    }


    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestPatentReprs() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKReprsData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));
        compareIntellectualPropertyEntityRepresentatives(originalPatent, comparePatent);
    }



    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentTestMainOwner() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKMainOwnerData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));

        assertNotNull(originalPatent.getMainOwner());
        assertNotNull(comparePatent);
        assertNotNull(comparePatent.getMainOwner());

        IpPersonAddresses originalAddress=originalPatent.getMainOwner();
        IpPersonAddresses compareAddress=comparePatent.getMainOwner();



        compareAddressAndPersonDataOrigin(originalAddress,compareAddress);
    }



    @Test
    @Transactional
    public void transformtoCoreTestCFileCPriorityData() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKPrioritiesData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        Gson gson = new Gson();
        System.out.println(gson.toJson(cPatent));

        checkTransformedPriorityData(originalPatent, cPatent.getFile());

    }

    @Test
    @Transactional
    public void transformIpMarkToCMarkTestCFileCSimpleProcessData() {
        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKPrioritiesData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        checkTransformedSimpleProcessData(originalPatent, cPatent.getFile());
    }



    @Test
    @Transactional
    public void transformtoCoreRevertToIpPatentPriorities() {

        IpPatent originalPatent = ipPatentRepository.getOne(ipFilePKPrioritiesData);
        CPatent cPatent = patentMapper.toCore(originalPatent, false);
        IpPatent comparePatent=patentMapper.toEntity(cPatent,false);

        Gson gson = new Gson();
        System.out.println(gson.toJson(comparePatent));
        assertNotNull(comparePatent);
        checkPriorities(originalPatent.getPriorities(), comparePatent.getPriorities());
    }









}
