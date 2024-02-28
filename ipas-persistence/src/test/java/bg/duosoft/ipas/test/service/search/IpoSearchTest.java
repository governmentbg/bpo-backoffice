package bg.duosoft.ipas.test.service.search;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.enums.SearchResultColumn;
import bg.duosoft.ipas.enums.SearchSortType;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.enums.search.PersonNameSearchType;
import bg.duosoft.ipas.enums.search.SearchOperatorTextType;
import bg.duosoft.ipas.enums.search.SearchOperatorType;
import org.hibernate.CacheMode;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Rollback
@Transactional
public class IpoSearchTest extends TestBase {
    private final static String FILE_SEQ = "BG";
    private final static String FILE_TYP_MARK = "N";
    private final static String FILE_TYP_PATENT = "P";
    private final static int FILE_SER_MARK = 2018;
    private final static int FILE_SER_PATENT = 1937;
    private final static String IP_FILE_PK_PATENT_TITLE = "ВРАТА";
    private final static String PATENT_SUMMARY = "сензорът на Хол";

    @Autowired
    private IpProcRepository ipProcRepository;

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @Autowired
    private IpoSearchService searchService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    @Qualifier(IpasDatabaseConfig.IPAS_ENTITY_MANAGER)
    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    private IndexService indexService;

    private IpFilePK ipFilePK0;
    private IpFilePK ipFilePK1;
    private IpFilePK ipFilePK2;
    private IpFilePK ipFilePK3;
    private IpFilePK ipFilePK4;
    private IpFilePK ipFilePK5;
    private IpFilePK ipFilePK6;
    private IpFilePK ipFilePK7;
    private IpFilePK ipFilePK8;

    private IpProcPK ipProcPK;

    // Patents
    private IpFilePK ipFilePK9;
    private IpFilePK ipFilePK10;
    private IpFilePK ipFilePK11;
    private IpFilePK ipFilePK12;
    private IpFilePK ipFilePK13;
    private IpFilePK ipFilePK14;
    private IpFilePK ipFilePK15;
    private IpFilePK ipFilePK16;
    private IpPatentSummaryPK ipPatentSummaryPK;

    private IpProcPK ipProcPK1;
    private IpProcPK ipProcPK2;
    private IpProcPK ipProcPK3;
    private IpProcPK ipProcPK4;

    private IpPatentIpcClassesPK ipPatentIpcClassesPK1;

    private static boolean dataLoaded = false;

    @Before
    public void setUp() {
        if (!dataLoaded) {
            // Marks
            indexService.delete(IpMark.class);
            ipFilePK0 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152976);
            indexService.index(ipFilePK0, IpMark.class);

            ipFilePK1 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152975);
            indexService.index(ipFilePK1, IpMark.class);

            ipFilePK2 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152974);
            indexService.index(ipFilePK2, IpMark.class);

            ipFilePK3 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152973);
            indexService.index(ipFilePK3, IpMark.class);

            ipFilePK4 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152967);
            indexService.index(ipFilePK4, IpMark.class);

            ipFilePK5 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152966);
            indexService.index(ipFilePK5, IpMark.class);

            ipFilePK6 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 151199);
            indexService.index(ipFilePK6, IpMark.class);

            ipFilePK7 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 149892);
            indexService.index(ipFilePK7, IpMark.class);

            ipFilePK8 = new IpFilePK(FILE_SEQ, "D", FILE_SER_MARK, 149892);
            indexService.index(ipFilePK8, IpMark.class);

            // Patents
            indexService.delete(IpPatent.class);
            ipFilePK9 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, FILE_SER_PATENT, 29479);
            indexService.index(ipFilePK9, IpPatent.class);

            ipFilePK10 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2018, 112736);
            indexService.index(ipFilePK10, IpPatent.class);

            ipFilePK11 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2016, 112320);
            indexService.index(ipFilePK11, IpPatent.class);

            ipFilePK12 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2014, 111705);
            indexService.index(ipFilePK12, IpPatent.class);

            ipFilePK13 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2018, 112804);
            indexService.index(ipFilePK13, IpPatent.class);

            ipFilePK14 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 1979, 44710);
            indexService.index(ipFilePK14, IpPatent.class);

            ipFilePK15 = new IpFilePK(FILE_SEQ, "С", 2002, 389);
            indexService.index(ipFilePK15, IpPatent.class);

            ipFilePK16 = new IpFilePK(FILE_SEQ, "С", 2002, 386);
            indexService.index(ipFilePK16, IpPatent.class);

            IpFilePK ipFilePK17 = new IpFilePK(FILE_SEQ, "P", 1976, 34337);
            indexService.index(ipFilePK17, IpPatent.class);

            ipFilePK17 = new IpFilePK(FILE_SEQ, "P", 1976, 35032);
            indexService.index(ipFilePK17, IpPatent.class);

            // designs with locarno classes
            indexService.index(new IpFilePK(FILE_SEQ, "Е", 2018, 11430012), IpPatent.class);
            indexService.index(new IpFilePK(FILE_SEQ, "Д", 2018, 11430), IpPatent.class);

            indexService.index(new IpFilePK(FILE_SEQ, "Е", 2018, 11431001), IpPatent.class);
            indexService.index(new IpFilePK(FILE_SEQ, "Д", 2018, 11431), IpPatent.class);

            indexService.index(new IpFilePK(FILE_SEQ, "Е", 2018, 11553008), IpPatent.class);
            indexService.index(new IpFilePK(FILE_SEQ, "Д", 2018, 11553), IpPatent.class);

            // Patent summary
            indexService.delete(IpPatentSummary.class);
            ipPatentSummaryPK = new IpPatentSummaryPK(FILE_SEQ, FILE_TYP_PATENT, 2018, 112804, "MX");
            indexService.index(ipPatentSummaryPK, IpPatentSummary.class);

            // Persons
            indexService.delete(IpPersonAddresses.class);
            // Mark's persons
            indexService.index(new IpPersonAddressesPK(398199, 1), IpPersonAddresses.class);
            indexService.index(new IpPersonAddressesPK(142868, 1), IpPersonAddresses.class);
            // Patent's persons
            indexService.index(new IpPersonAddressesPK(286168, 1), IpPersonAddresses.class);
            indexService.index(new IpPersonAddressesPK(286168, 2), IpPersonAddresses.class);
            indexService.index(new IpPersonAddressesPK(286168, 3), IpPersonAddresses.class);
            indexService.index(new IpPersonAddressesPK(283816, 1), IpPersonAddresses.class);
            indexService.index(new IpPersonAddressesPK(261806, 1), IpPersonAddresses.class);

            // Process
            indexService.delete(IpProc.class);
            // Mark's processes
            ipProcPK = new IpProcPK("2", 910892);
            indexService.index(ipProcPK, IpProc.class);
            // Patent's processes
            ipProcPK1 = new IpProcPK("10", 394864);
            indexService.index(ipProcPK1, IpProc.class);
            ipProcPK2 = new IpProcPK("10", 930939);
            indexService.index(ipProcPK2, IpProc.class);
            ipProcPK3 = new IpProcPK("10", 601304);
            indexService.index(ipProcPK3, IpProc.class);
            ipProcPK4 = new IpProcPK("2", 482566);
            indexService.index(ipProcPK4, IpProc.class);

            // Actions
            indexService.delete(IpAction.class);
            IpProc proc = ipProcRepository.findById(ipProcPK3).orElseGet(null);
            List<IpAction> ipActions = proc.getIpActions();
            for (IpAction action : ipActions) {
                indexService.index(action.getPk(), IpAction.class);
            }
            // Mark's actions
            IpProc proc2 = ipProcRepository.findById(ipProcPK).orElseGet(null);
            List<IpAction> ipActions2 = proc2.getIpActions();
            for (IpAction action : ipActions2) {
                indexService.index(action.getPk(), IpAction.class);
            }
            // Patent's actions
            IpProc proc3 = ipProcRepository.findById(ipProcPK2).orElseGet(null);
            List<IpAction> ipActions3 = proc3.getIpActions();
            for (IpAction action : ipActions3) {
                indexService.index(action.getPk(), IpAction.class);
            }
            IpProc proc4 = ipProcRepository.findById(ipProcPK3).orElseGet(null);
            List<IpAction> ipActions4 = proc4.getIpActions();
            for (IpAction action : ipActions4) {
                indexService.index(action.getPk(), IpAction.class);
            }

            // Mark nice classes
            IpMark ipMark = ipMarkRepository.findById(ipFilePK6).orElse(null);
            for (IpMarkNiceClasses niceClass : ipMark.getIpMarkNiceClasses()) {
                indexService.index(niceClass.getPk(), IpMarkNiceClasses.class);
            }
            IpMark ipMark1 = ipMarkRepository.findById(ipFilePK5).orElse(null);
            for (IpMarkNiceClasses niceClass : ipMark1.getIpMarkNiceClasses()) {
                indexService.index(niceClass.getPk(), IpMarkNiceClasses.class);
            }

            // Mark vienna classes
            for (IpLogoViennaClasses viennaClasses : ipMark.getLogo().getIpLogoViennaClassesCollection()) {
                indexService.index(viennaClasses.getPk(), IpLogoViennaClasses.class);
            }
            IpMark ipMark2 = ipMarkRepository.findById(ipFilePK4).orElse(null);
            for (IpLogoViennaClasses viennaClasses : ipMark2.getLogo().getIpLogoViennaClassesCollection()) {
                indexService.index(viennaClasses.getPk(), IpLogoViennaClasses.class);
            }

            // Patent ipc classes
            indexService.delete(IpPatentIpcClasses.class);
            ipPatentIpcClassesPK1 = new IpPatentIpcClassesPK(
                    FILE_SEQ,
                    FILE_TYP_PATENT,
                    1979,
                    44710,
                    "2",
                    "A",
                    "01",
                    "G",
                    "25",
                    "00",
                    "1");

            indexService.index(ipPatentIpcClassesPK1, IpPatentIpcClasses.class);

            IpPatentIpcClassesPK ipPatentIpcClassesPK2 = new IpPatentIpcClassesPK(
                    FILE_SEQ,
                    FILE_TYP_PATENT,
                    2014,
                    111705,
                    "8",
                    "b",
                    "60",
                    "w",
                    "10",
                    "26",
                    "2");

            indexService.index(ipPatentIpcClassesPK2, IpPatentIpcClasses.class);

            ipPatentIpcClassesPK2 = new IpPatentIpcClassesPK(
                    FILE_SEQ,
                    FILE_TYP_PATENT,
                    2014,
                    111705,
                    "8",
                    "H",
                    "02",
                    "P",
                    "27",
                    "00",
                    "1");

            indexService.index(ipPatentIpcClassesPK2, IpPatentIpcClasses.class);

            indexService.delete(IpDoc.class);
            indexService.delete(IpUserdoc.class);
            IpDocPK ipDocPK = new IpDocPK("BG", "E", 2002, 1322309);
            indexService.index(ipDocPK, IpDoc.class);
            IpDocPK ipDocPK1 = new IpDocPK("BG", "E", 2002, 1322297);
            indexService.index(ipDocPK1, IpDoc.class);
            IpDocPK ipDocPK2 = new IpDocPK("BG", "E", 2017, 1269115);
            indexService.index(ipDocPK2, IpDoc.class);
            IpDocPK ipDocPK3 = new IpDocPK("BG", "E", 2014, 986121);
            indexService.index(ipDocPK3, IpDoc.class);
            indexService.index(ipDocPK3, IpUserdoc.class);
            IpDocPK ipDocPK4 = new IpDocPK("BG", "E", 2014, 986122);
            indexService.index(ipDocPK4, IpDoc.class);
            indexService.index(ipDocPK4, IpUserdoc.class);
            IpDocPK ipDocPK5 = new IpDocPK("BG", "E", 2016, 1159009);
            indexService.index(ipDocPK5, IpDoc.class);
            indexService.index(ipDocPK5, IpUserdoc.class);


            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
            try {
                fullTextEntityManager
                        .createIndexer(IpPatentLocarnoClasses.class)
                        .batchSizeToLoadObjects( 25 )
                        .cacheMode( CacheMode.NORMAL )
                        .threadsToLoadObjects( 5 )
                        .idFetchSize( 10 )
                        .startAndWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                fullTextEntityManager
                        .createIndexer(IpFileRelationship.class)
                        .batchSizeToLoadObjects( 25 )
                        .cacheMode( CacheMode.NORMAL )
                        .threadsToLoadObjects( 5 )
                        .idFetchSize( 10 )
                        .startAndWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            dataLoaded = true;
        }

    }

    @Test
    public void testSearchMarkByTitle() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .title("ПИВОВАРНА".toLowerCase())
                .titleTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByTitle1() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .title("методи".toLowerCase())
                .titleTypeSearch(SearchOperatorTextType.KEYWORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByTitle2() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .title("мето".toLowerCase())
                .titleTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByFileSeqTypeSerNbr() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .fileSeq("BG")
                .addFileType("N")
                .fromFileSer("2018")
                .toFileSer("2018")
                .fromFileNbr("149892")
                .toFileNbr("149892");
//        searchParam.fileSeq("BG").fromFileNbr("1").toFileNbr("1000000");
        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByFileSeqSerNbr() {
        CSearchParam searchParam = new CSearchParam();
        searchParam.fileSeq("BG").addFileType("D").addFileType("N").fromFileSer("2018").toFileSer("2018").fromFileNbr("149892").toFileNbr("149892");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByFileNumber() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromFileNbr("152975")
                .toFileNbr("152975");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByFileNumberAbove() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromFileNbr("152975");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByFileNumberBelow() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .toFileNbr("152975");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByFilingDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("18.11.2018");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromFilingDate(dt)
                .toFilingDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByRegistrationNumber() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromRegistrationNbr("103531")
                .toRegistrationNbr("103531");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByRegistrationDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("07.11.2018");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromRegistrationDate(dt)
                .toRegistrationDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByInternationalRegistrationNumber() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromInternationalRegistrationNbr("149892")
                .toInternationalRegistrationNbr("149892");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByExpirationDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("27.06.2028");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromExpirationDate(dt)
                .toExpirationDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByEntitlementDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("27.06.2018");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fromEntitlementDate(dt)
                .toEntitlementDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByOwnerName() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .ownerName("ЖАСМИН")
                .ownerNameTypeSearch(PersonNameSearchType.CONTAINS_WORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByOwnerNameExpectZero() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .ownerName("asdadad");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByRepresentativeName() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .representativeName("Людмил Славчев Димитров")
                .representativeNameTypeSearch(PersonNameSearchType.CONTAINS_WORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByRepresentativeNameExpectZero() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .representativeName("asdasdasd");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByStatusCode() {
        CSearchParam searchParam = new CSearchParam();

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("043");

        searchParam
                .addFileType("D").addFileType("N").statusCodes(statusCodes);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByStatusCodeExpectZero() {
        CSearchParam searchParam = new CSearchParam();

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("2379");

        searchParam
                .addFileType("D").addFileType("N")
                .statusCodes(statusCodes);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByStatusCodeAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20181104");
        Date toStatusDate = sdf.parse("20181104");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("016");

        searchParam
                .addFileType("D").addFileType("N")
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByFirstStatusCodeAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20181104");
        Date toStatusDate = sdf.parse("20181104");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("016");

        searchParam
                .addFileType("D").addFileType("N")
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByLastStatusCodeAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20180627");
        Date toStatusDate = sdf.parse("20180627");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("003");

        searchParam
                .addFileType("D").addFileType("N")
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByLastStatusCodeAndDateRangeExpectZero() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20181110");
        Date toStatusDate = sdf.parse("20181110");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("043");

        searchParam
                .addFileType("D").addFileType("N")
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByActionTypesAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromActionDate = sdf.parse("20180629");
        Date toActionDate = sdf.parse("20180629");

        List<String> actionTypes = new ArrayList<>();
        actionTypes.add("1251");
        actionTypes.add("297");

        searchParam
                .addFileType("D").addFileType("N")
                .actionTypes(actionTypes)
                .fromActionDate(fromActionDate)
                .toActionDate(toActionDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByActionTypesAndDateRangeExpectZero() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromActionDate = sdf.parse("20181029");
        Date toActionDate = sdf.parse("20181029");

        List<String> actionTypes = new ArrayList<>();
        actionTypes.add("11764");
        actionTypes.add("2974");

        searchParam
                .addFileType("D").addFileType("N")
                .actionTypes(actionTypes)
                .fromActionDate(fromActionDate)
                .toActionDate(toActionDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByPersonNationality() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("D").addFileType("N")
                .ownerNationality("BG");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByPersonNationalityExpectZero() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("D").addFileType("N")
                .ownerNationality("GBR");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByAgentCode() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("D").addFileType("N")
                .agentCode("425");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByAgentCodeExpectZero() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("D").addFileType("N")
                .agentCode("589");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByPublication() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("D").addFileType("N")
                .publicationYear("2018")
                .publicationBulletin("07.2")
                .publicationSect("19");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByNiceClassAnd() {
        CSearchParam searchParam = new CSearchParam();
        List<String> niceClasses = new ArrayList<>();

        niceClasses.add("5");

        searchParam
                .addFileType("D").addFileType("N")
                .niceClasses(niceClasses)
                .niceClassesType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByNiceClassAnd2() {
        CSearchParam searchParam = new CSearchParam();
        List<String> niceClasses = new ArrayList<>();

        niceClasses.add("5");
        niceClasses.add("43");

        searchParam
                .addFileType("D").addFileType("N")
                .niceClasses(niceClasses)
                .niceClassesType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByNiceClassNot() {
        CSearchParam searchParam = new CSearchParam();
        List<String> niceClasses = new ArrayList<>();

        niceClasses.add("5");

        searchParam
                .addFileType("D").addFileType("N")
                .niceClasses(niceClasses)
                .niceClassesType(SearchOperatorType.AND_NOT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByNiceClassNot2() {
        CSearchParam searchParam = new CSearchParam();
        List<String> niceClasses = new ArrayList<>();

        niceClasses.add("5");
        niceClasses.add("43");

        searchParam
                .addFileType("D").addFileType("N")
                .niceClasses(niceClasses)
                .niceClassesType(SearchOperatorType.AND_NOT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(7, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByNiceClassOr() {
        CSearchParam searchParam = new CSearchParam();
        List<String> niceClasses = new ArrayList<>();

        niceClasses.add("5");
        niceClasses.add("43");

        searchParam
                .addFileType("D").addFileType("N")
                .niceClasses(niceClasses)
                .niceClassesType(SearchOperatorType.OR);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClass() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1.18");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.AND);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassWithWildcard() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1*");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassNot() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1.18");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.AND_NOT);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassNot2() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1.18");
        viennaClassCodes.add("27.5.1");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.AND_NOT);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(7, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassAnd() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1.18");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.AND);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassAnd2() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("27.5.1");
        viennaClassCodes.add("27.5.3");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.AND);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassAnd2Zero() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1.18");
        viennaClassCodes.add("27.1.5");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.AND);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassOr() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1.18");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.OR);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchMarkByViennaClassOr2() {
        CSearchParam searchParam = new CSearchParam();
        List<String> viennaClassCodes = new ArrayList<>();

        viennaClassCodes.add("26.1.18");
        viennaClassCodes.add("27.5.1");

        searchParam
                .addFileType("D").addFileType("N")
                .viennaClassCodes(viennaClassCodes)
                .viennaClassCodeType(SearchOperatorType.OR);


        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchMarkBySignCode() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .signCode("D");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(4, result.getTotalElements());
    }

    @Test
    public void testSearchMarkBySignCodeHasImg() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .signCode("M");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(5, result.getTotalElements());

        List<CSearchResult> content = result.getContent();
        assertTrue(content.get(0).getHasImg());
        assertTrue(content.get(1).getHasImg());
        assertTrue(content.get(2).getHasImg());
        assertTrue(content.get(3).getHasImg());
        assertTrue(content.get(4).getHasImg());
    }

    // Patents
    @Test
    public void testSearchPatentByTitle() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .title(IP_FILE_PK_PATENT_TITLE.toLowerCase());

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByTitle1() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .title(IP_FILE_PK_PATENT_TITLE.toLowerCase())
                .titleTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByTitle2() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .title("МЕТОД ЗА ПРИЛОЖЕНИЕТО".toLowerCase())
                .titleTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByTitle3() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .title("ТОД ЗА ПРИЛОЖЕНИЕ".toLowerCase())
                .titleTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByEnglishTitle() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .englishTitle("A SECTIONED DOOR USED FOR ADVERTISEMENT")
                .englishTitleTypeSearch(SearchOperatorTextType.KEYWORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByEnglishTitle1() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .englishTitle("SECTIONED DOOR ADVERTISEMENT")
                .englishTitleTypeSearch(SearchOperatorTextType.KEYWORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByEnglishTitle2() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .englishTitle("SECTIONED DOOR ADVERTISEMENT")
                .englishTitleTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByEnglishTitle3() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .englishTitle("ONED DO")
                .englishTitleTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByFileNumber() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromFileNbr("112320")
                .toFileNbr("112320");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByFileNumberRange() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromFileNbr("1")
                .toFileNbr("110000");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(4, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByFilingDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("22.06.1937");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromFilingDate(dt)
                .toFilingDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRegistrationNumber() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromRegistrationNbr("66652")
                .toRegistrationNbr("66652");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }


    @Test
    public void testSearchPatentByRegistrationNumberRange() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromRegistrationNbr("1")
                .toRegistrationNbr("3");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRegistrationNumberRange1() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromRegistrationNbr("a")
                .toRegistrationNbr("3");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRegistrationNumberRange2() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromRegistrationNbr("1")
                .toRegistrationNbr("a");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRegistrationNumberRange3() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromRegistrationNbr("2");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(5, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRegistrationNumberRange4() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .toRegistrationNbr("1");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRegistrationDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("30.03.2018");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromRegistrationDate(dt)
                .toRegistrationDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByExpirationDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("14.06.2036");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromExpirationDate(dt)
                .toExpirationDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByEntitlementDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("14.06.2016");
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .fromEntitlementDate(dt)
                .toEntitlementDate(dt);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByOwnerName() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ownerName("Сид Ентърпрайз")
                .ownerNameTypeSearch(PersonNameSearchType.CONTAINS_WORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByOwnerNameExpectZero() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ownerName("asdadad");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRepresentativeName() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .representativeName("Силвия Христова Тодорова")
                .representativeNameTypeSearch(PersonNameSearchType.CONTAINS_WORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRepresentativeNameExpectZero() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .representativeName("asdasdasd");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByInventorName() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .inventorName("светослав светославов забунов")
                .inventorNameTypeSearch(PersonNameSearchType.CONTAINS_WORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }


    @Test
    public void testSearchPatentByInventorNameExpectZero() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .inventorName("asfasdfsafdas");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByStatusCode() {
        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("237");

        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .statusCodes(statusCodes);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByStatusCodeExpectZero() {
        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("2379");

        CSearchParam searchParam = new CSearchParam();
        searchParam
                .addFileType(FILE_TYP_PATENT)
                .statusCodes(statusCodes);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByStatusCodeAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20181025");
        Date toStatusDate = sdf.parse("20181025");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("197");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByFirstStatusCodeAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20180919");
        Date toStatusDate = sdf.parse("20180919");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("183");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByLastStatusCodeAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20181029");
        Date toStatusDate = sdf.parse("20181029");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("200");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByLastStatusCodeAndDateRangeExpectZero() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromStatusDate = sdf.parse("20181029");
        Date toStatusDate = sdf.parse("20181029");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("205");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByActionTypesAndDateRange() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromActionDate = sdf.parse("20181029");
        Date toActionDate = sdf.parse("20181029");

        List<String> actionTypes = new ArrayList<>();
        actionTypes.add("1176");
        actionTypes.add("297");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .actionTypes(actionTypes)
                .fromActionDate(fromActionDate)
                .toActionDate(toActionDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByActionTypesAndDateRangeExpectZero() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date fromActionDate = sdf.parse("20181028");
        Date toActionDate = sdf.parse("20181028");

        List<String> actionTypes = new ArrayList<>();
        actionTypes.add("11764");
        actionTypes.add("2974");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .actionTypes(actionTypes)
                .fromActionDate(fromActionDate)
                .toActionDate(toActionDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByPersonNationality() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ownerNationality("BG");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByPersonNationalityExpectZero() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ownerNationality("GBR");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByAgentCode() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .agentCode("581");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByAgentCodeExpectZero() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .agentCode("589");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByPatentSummaryKeywords() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .patentSummary(PATENT_SUMMARY)
                .patentSummaryTypeSearch(SearchOperatorTextType.KEYWORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByPatentSummaryContainsString() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .patentSummary("зорът на Хо")
                .patentSummaryTypeSearch(SearchOperatorTextType.CONTAINS_STRING);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByPatentSummaryExpectZero() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .patentSummary("adadasdasd");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByPublication() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .publicationYear("2018")
                .publicationBulletin("03.2")
                .publicationSect("4");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByIpcClass()  {
        CSearchParam searchParam = new CSearchParam();
        List<String> ipcClassес = new ArrayList<>();

        ipcClassес.add("A01G2500");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ipcClasses(ipcClassес)
                .ipcClassType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByIpcClassCode() {
        CSearchParam searchParam = new CSearchParam();
        List<String> ipcClassеs = new ArrayList<>();

        ipcClassеs.add("A01G*");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ipcClasses(ipcClassеs)
                .ipcClassType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByIpcClassAnd2()  {
        CSearchParam searchParam = new CSearchParam();
        List<String> ipcClassес = new ArrayList<>();

        ipcClassес.add("b60w1026");
        ipcClassес.add("H02P2700");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ipcClasses(ipcClassес)
                .ipcClassType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByIpcClassOr2()  {
        CSearchParam searchParam = new CSearchParam();
        List<String> ipcClassес = new ArrayList<>();

        ipcClassес.add("A01G2500");
        ipcClassес.add("H02P2700");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ipcClasses(ipcClassес)
                .ipcClassType(SearchOperatorType.OR);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByIpcClassNot2()  {
        CSearchParam searchParam = new CSearchParam();
        List<String> ipcClassес = new ArrayList<>();

        ipcClassес.add("A01G2500");
        ipcClassес.add("H02P2700");

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .ipcClasses(ipcClassес)
                .ipcClassType(SearchOperatorType.AND_NOT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(6, result.getTotalElements());
    }

    @Test
    public void testSearchPatentSortByPk() {
        CSearchParam searchParam = new
                CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .sortOrder(SearchSortType.ASC.toString())
                .sortColumn(SearchResultColumn.PK_SORT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchPatentSortByFilingDate() throws ParseException {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .sortOrder(SearchSortType.ASC.toString())
                .sortColumn(SearchResultColumn.FILING_DATA_SORT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());

        List<CSearchResult> content = result.getContent();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        assertEquals(sdf.parse("22-06-1937 00:00:00"), content.get(0).getFilingDate());
        assertEquals(sdf.parse("01-10-1976 00:00:00"), content.get(1).getFilingDate());
        assertEquals(sdf.parse("28-12-1976 00:00:00"), content.get(2).getFilingDate());
        assertEquals(sdf.parse("21-08-1979 00:00:00"), content.get(3).getFilingDate());
        assertEquals(sdf.parse("20-02-2014 00:00:00"), content.get(4).getFilingDate());
        assertEquals(sdf.parse("14-06-2016 00:00:00"), content.get(5).getFilingDate());
        assertEquals(sdf.parse("15-05-2018 00:00:00"), content.get(6).getFilingDate());
        assertEquals(sdf.parse("19-09-2018 00:00:00"), content.get(7).getFilingDate());
    }

    @Test
    public void testSearchPatentSortByRegistrationNbr() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .sortOrder(SearchSortType.ASC.toString())
                .sortColumn(SearchResultColumn.REGISTRATION_NBR_SORT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchPatentSortByServicePerson() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .sortOrder(SearchSortType.ASC.toString())
                .sortColumn(SearchResultColumn.SERVICE_PERSON_SORT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchPatentSortByTitle() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType(FILE_TYP_PATENT)
                .sortOrder(SearchSortType.ASC.toString())
                .sortColumn(SearchResultColumn.TITLE_SORT);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByBulClassify() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .taxon("Слънчоглед");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByEngClassify() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .taxon("Helianthus annuus L.");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByProposedDenomination() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .proposedDenomination("ХИБРИД МУСАЛА");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByProposedDenominationEng() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .proposedDenominationEng("HIBRID MUSALA");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByPublDenomination() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .publDenomination("ХИБРИД МУСАЛА");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByPublDenominationEng() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .publDenominationEng("HIBRID MUSALA");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByApprDenomination() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .apprDenomination("ХИБРИД МУСАЛА");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByApprDenominationEng() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .apprDenominationEng("HIBRID MUSALA");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByRejDenomination() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .rejDenomination("PRUNUS GI 148/2");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByRejDenominationEng() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .rejDenominationEng("PRUNUS GI 148/2");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByFeatures() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .features("Мусала спада към групата на средноранните хибриди-узрява 5-6 дни след хибрид Албена.");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByStability() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .stability("хибрид Мусала притежава устойчивост на нови раса мана и толерантност на фомопсис");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByTesting() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .testing("технологията на отглеждане е същата както при хибрид Албена");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByTitle() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .title("ХИБРИД МУСАЛА")
                .titleTypeSearch(SearchOperatorTextType.KEYWORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPlantByEnglishTitle() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .englishTitle("HIBRID MUSALA")
                .englishTitleTypeSearch(SearchOperatorTextType.KEYWORDS);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK1() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromFilingDate = sdf.parse("27.12.2002");
        Date toFilingDate = sdf.parse("27.12.2002");

        searchParam
                .fromRequestForValidationNbr("18414")
                .toRequestForValidationNbr("18414")
                .fromRequestForValidationDate(fromFilingDate)
                .toRequestForValidationDate(toFilingDate)
        ;

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK2() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromFilingDate = sdf.parse("27.12.2002");
        Date toFilingDate = sdf.parse("27.12.2002");

        searchParam
                .fromRequestForValidationNbr("18417")
                .fromRequestForValidationDate(fromFilingDate)
                .toRequestForValidationDate(toFilingDate)
        ;

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK3() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromFilingDate = sdf.parse("27.12.2002");
        Date toFilingDate = sdf.parse("27.12.2002");

        searchParam
                .toRequestForValidationNbr("18410")
                .fromRequestForValidationDate(fromFilingDate)
                .toRequestForValidationDate(toFilingDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK4() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromFilingDate = sdf.parse("27.12.2002");
        Date toFilingDate = sdf.parse("27.12.2002");

        searchParam
                .fromRequestForValidationDate(fromFilingDate)
                .toRequestForValidationDate(toFilingDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK5() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromFilingDate = sdf.parse("26.12.2002");
        Date toFilingDate = sdf.parse("26.12.2002");

        searchParam
                .fromRequestForValidationDate(fromFilingDate)
                .toRequestForValidationDate(toFilingDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK6() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromFilingDate = sdf.parse("26.12.2002");

        searchParam
                .fromRequestForValidationDate(fromFilingDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK7() throws ParseException {
        CSearchParam searchParam = new CSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date toFilingDate = sdf.parse("26.12.2002");

        searchParam.toRequestForValidationDate(toFilingDate);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByIpDocPK8() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .toRequestForValidationNbr("18410")
                .requestForValidationType("ОК");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByLocarnoClass() {
        CSearchParam searchParam = new CSearchParam();
        List<String> locarnoClasses = new ArrayList<>();
        locarnoClasses.add("06-04");

        searchParam
                .addFileType("Д")
                .locarnoClasses(locarnoClasses)
                .locarnoClassCodeType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByLocarnoClassesAndExpectZero() {
        CSearchParam searchParam = new CSearchParam();
        List<String> locarnoClasses = new ArrayList<>();
        locarnoClasses.add("06-04");
        locarnoClasses.add("06-01");

        searchParam
                .addFileType("Д")
                .locarnoClasses(locarnoClasses)
                .locarnoClassCodeType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchByLocarnoClassesAndExpectOne() {
        CSearchParam searchParam = new CSearchParam();
        List<String> locarnoClasses = new ArrayList<>();
        locarnoClasses.add("09-03");
        locarnoClasses.add("19-08");

        searchParam
                .addFileType("Д")
                .locarnoClasses(locarnoClasses)
                .locarnoClassCodeType(SearchOperatorType.AND);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByLocarnoClassesOrExpectTwo() {
        CSearchParam searchParam = new CSearchParam();
        List<String> locarnoClasses = new ArrayList<>();
        locarnoClasses.add("06-04");
        locarnoClasses.add("06-01");

        searchParam
                .addFileType("Д")
                .locarnoClasses(locarnoClasses)
                .locarnoClassCodeType(SearchOperatorType.OR);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchByLocarnoClassesAndNotExpectOne() {
        CSearchParam searchParam = new CSearchParam();
        List<String> locarnoClasses = new ArrayList<>();
        locarnoClasses.add("06-04");
        locarnoClasses.add("06-01");

        searchParam
                .addFileType("Д")
                .locarnoClasses(locarnoClasses)
                .locarnoClassCodeType(SearchOperatorType.OR);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }


    @Test
    public void testSearchByAppTypeAndExpectTwo() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("P")
                .applTyp("АСД");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchBySubAppTypeAndExpectOne() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("P")
                .subApplTyp("АС");

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByResponsibleUserIdAndExpectTwo() {
        CSearchParam searchParam = new CSearchParam();

        searchParam
                .addFileType("P")
                .responsibleUserId(1);

        Page<CSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }
}
