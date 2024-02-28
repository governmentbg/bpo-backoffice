package bg.duosoft.ipas.test.service.search;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.model.util.UserDocSearchResult;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.UserDocSearchService;
import bg.duosoft.ipas.enums.SearchResultColumn;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPersonPK;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.core.model.search.CUserdocSearchParam;
import bg.duosoft.ipas.enums.search.PersonNameSearchType;
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

import static org.junit.Assert.*;

@Rollback
@Transactional
public class UserDocsSearchTest extends TestBase {

    @Autowired
    private IpProcRepository ipProcRepository;

    @Autowired
    private UserDocSearchService searchService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    @Qualifier(IpasDatabaseConfig.IPAS_ENTITY_MANAGER)
    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    private IndexService indexService;

    private static boolean dataLoaded = false;

    @Before
    public void setUp() {
        if (!dataLoaded) {

            indexService.delete(IpUserdoc.class);
            indexService.delete(IpDoc.class);
            IpDocPK ipDocPK = new IpDocPK("BG", "E", 2014, 986121);
            indexService.index(ipDocPK, IpDoc.class);
            indexService.index(ipDocPK, IpUserdoc.class);

            IpDocPK ipDocPK1 = new IpDocPK("BG", "E", 2014, 986122);
            indexService.index(ipDocPK1, IpDoc.class);
            indexService.index(ipDocPK1, IpUserdoc.class);

            IpDocPK ipDocPK2 = new IpDocPK("BG", "E", 2016, 1159009);
            indexService.index(ipDocPK2, IpDoc.class);
            indexService.index(ipDocPK2, IpUserdoc.class);

            indexService.delete(IpUserdocPerson.class);
            IpUserdocPersonPK ipUserdocPersonPK = new IpUserdocPersonPK(
                    "BG",
                    "E",
                    2014,
                    986122,
                    106801,
                    1,
                    UserdocPersonRole.valueOf("APPLICANT"));
            indexService.index(ipUserdocPersonPK, IpUserdocPerson.class);
            IpUserdocPersonPK ipUserdocPersonPK1 = new IpUserdocPersonPK(
                    "BG",
                    "E",
                    2014,
                    986122,
                    106801,
                    1,
                    UserdocPersonRole.valueOf("NEW_OWNER"));
            indexService.index(ipUserdocPersonPK1, IpUserdocPerson.class);


            indexService.delete(IpProc.class);
            // Mark's processes
            IpProcPK ipProcPK = new IpProcPK("6", 613631);
            indexService.index(ipProcPK, IpProc.class);

            indexService.delete(IpAction.class);
            IpProc proc = ipProcRepository.findById(ipProcPK).orElseGet(null);
            List<IpAction> ipActions = proc.getIpActions();
            for (IpAction action : ipActions) {
                indexService.index(action.getPk(), IpAction.class);
            }

//            indexService.delete(IpUserdoc.class);
//            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
//            try {
//                fullTextEntityManager
//                        .createIndexer(IpUserdoc.class)
//                        .batchSizeToLoadObjects( 25 )
//                        .cacheMode( CacheMode.NORMAL )
//                        .threadsToLoadObjects( 5 )
//                        .idFetchSize( 10 )
//                        .startAndWait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
            indexService.delete(IpPersonAddresses.class);
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
            try {
                fullTextEntityManager
                        .createIndexer(IpPersonAddresses.class)
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
    public void testSearchByUserDocType() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam.userDocType("ПРМ");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByFileType() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam.fileType("N");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchByDocNumber() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam.docNumber("BG/E/2016/1331335");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByDocNumberPart() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam.docNumber("BG/E/2016/133133");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByFilingDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dt = sdf.parse("05.08.2016");

        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam
                .fromFilingDate(dt)
                .toFilingDate(dt);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }


    @Test
    public void testSearchPatentByStatusCode() {
        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("049");

        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam
                .statusCodes(statusCodes);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByStatusCodeExpectZero() {
        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("2379");

        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam
                .statusCodes(statusCodes);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByStatusCodeAndDateRange() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromStatusDate = sdf.parse("09.08.2016");
        Date toStatusDate = sdf.parse("09.08.2016");

        List<String> statusCodes = new ArrayList<>();
        statusCodes.add("048");

        CUserdocSearchParam searchParam = new CUserdocSearchParam();

        searchParam
                .statusCodes(statusCodes)
                .fromStatusDate(fromStatusDate)
                .toStatusDate(toStatusDate);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByActionTypes() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();

        List<String> actionTypes = new ArrayList<>();
        actionTypes.add("083");

        searchParam
                .actionTypes(actionTypes);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByActionTypesAndDateRange() throws ParseException {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date fromActionDate = sdf.parse("09.08.2016");
        Date toActionDate = sdf.parse("09.08.2016");

        List<String> actionTypes = new ArrayList<>();
        actionTypes.add("066");

        searchParam
                .actionTypes(actionTypes)
                .fromActionDate(fromActionDate)
                .toActionDate(toActionDate);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonName() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam
                .personName("Akzo Nobel Coatings International B.V")
                .personNameSearchType(PersonNameSearchType.CONTAINS_WORDS);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByRole() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();

        searchParam
                .role("APPLICANT");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchResult() throws ParseException {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();

        searchParam
                .role("APPLICANT");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());

        UserDocSearchResult userDocSearchResult = result.getContent().get(0);
        assertEquals("BG", userDocSearchResult.getFileId().getFileSeq());
        assertEquals("N", userDocSearchResult.getFileId().getFileType());
        assertEquals(2001L, userDocSearchResult.getFileId().getFileSeries().longValue());
        assertEquals(54072L, userDocSearchResult.getFileId().getFileNbr().longValue());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date filingData = sdf.parse("31.07.2014");
        assertEquals(filingData, userDocSearchResult.getFilingDate());
        assertEquals("Искане за вписване на прехвърляне на правото върху обект на ИС", userDocSearchResult.getDocType());
        List<String> applicants = userDocSearchResult.getApplicants();
        for (String applicant:applicants) {
            assertEquals("Akzo Nobel Coatings International B.V.", applicant);
        }
    }

    @Test
    public void testSearchSortByFilingDate() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam
                .fileType("N")
                .sortColumn(SearchResultColumn.DOC_FILING_DATA_SORT)
                .sortOrder("asc");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());

        List<UserDocSearchResult> content = result.getContent();
        Date filingDate1 = content.get(0).getFilingDate();
        Date filingDate2 = content.get(1).getFilingDate();

        searchParam
                .fileType("N")
                .sortColumn(SearchResultColumn.DOC_FILING_DATA_SORT)
                .sortOrder("desc");

        result = searchService.search(searchParam);
        content = result.getContent();

        assertEquals(filingDate2, content.get(0).getFilingDate());
        assertEquals(filingDate1, content.get(1).getFilingDate());
    }

    @Test
    public void testSearchSortByDocNumber() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam
                .sortColumn(SearchResultColumn.DOC_NUMBER)
                .sortOrder("asc");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(3, result.getTotalElements());

        List<UserDocSearchResult> content = result.getContent();
        String docNumber1 = content.get(0).getDocNumber();
        String docNumber2 = content.get(1).getDocNumber();
        String docNumber3 = content.get(2).getDocNumber();

        searchParam
                .sortColumn(SearchResultColumn.DOC_NUMBER)
                .sortOrder("desc");

        result = searchService.search(searchParam);
        content = result.getContent();

        assertNotNull(docNumber3);
        assertNotNull( content.get(0).getDocNumber());
        assertEquals(docNumber3, content.get(0).getDocNumber());
        assertNull(docNumber2);
        assertNull(content.get(1).getDocNumber());
        assertNull(docNumber1);
        assertNull(content.get(2).getDocNumber());
    }

    @Test
    public void testSearchSortByFilePK() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();
        searchParam
                .sortColumn(SearchResultColumn.PK_SORT)
                .sortOrder("asc");

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(3, result.getTotalElements());

        List<UserDocSearchResult> content = result.getContent();
        Date filingDate1 = content.get(0).getFilingDate();
        Date filingDate2 = content.get(1).getFilingDate();
        Date filingDate3 = content.get(2).getFilingDate();

        searchParam
                .sortColumn(SearchResultColumn.PK_SORT)
                .sortOrder("desc");

        result = searchService.search(searchParam);
        content = result.getContent();

        assertEquals(filingDate3, content.get(0).getFilingDate());
        assertEquals(filingDate2, content.get(1).getFilingDate());
        assertEquals(filingDate1, content.get(2).getFilingDate());
    }


    @Test
    public void testSearchByResponsibleUserId() {
        CUserdocSearchParam searchParam = new CUserdocSearchParam();

        searchParam
                .responsibleUserId(1360);

        Page<UserDocSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
    }
}
