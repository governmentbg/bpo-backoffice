package bg.duosoft.ipas.test.service.search;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.util.AutocompleteIpoSearchResult;
import bg.duosoft.ipas.core.service.search.AutocompleteIpoSearchService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.patent.IpPatentRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.core.model.search.AutocompleteIpoSearchParam;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Rollback
@Transactional
public class AutocompleteIpoSearchTest extends TestBase {
    private final static String FILE_SEQ = "BG";
    private final static String FILE_TYP_MARK = "N";
    private final static String FILE_TYP_PATENT = "P";
    private final static int FILE_SER_MARK = 2018;
    private final static int FILE_SER_PATENT = 1937;

    @Autowired
    private IpMarkRepository ipMarkRepository;

    @Autowired
    private IpPatentRepository ipPatentRepository;

    @Autowired
    private AutocompleteIpoSearchService searchService;

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

    // Patents
    private IpFilePK ipFilePK9;
    private IpFilePK ipFilePK10;
    private IpFilePK ipFilePK11;
    private IpFilePK ipFilePK12;
    private IpFilePK ipFilePK13;
    private IpFilePK ipFilePK14;

    private static boolean dataLoaded = false;

    @Before
    public void setUp() {
        // Marks
        ipFilePK0 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152976);
        ipFilePK1 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152975);
        ipFilePK2 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152974);
        ipFilePK3 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152973);
        ipFilePK4 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152967);
        ipFilePK5 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 152966);
        ipFilePK6 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 151199);
        ipFilePK7 = new IpFilePK(FILE_SEQ, FILE_TYP_MARK, FILE_SER_MARK, 149892);
        ipFilePK8 = new IpFilePK(FILE_SEQ, "D", FILE_SER_MARK, 135920);
        // Patents
        ipFilePK9 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, FILE_SER_PATENT, 29479);
        ipFilePK10 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2018, 112736);
        ipFilePK11 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2016, 112320);
        ipFilePK12 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2014, 111705);
        ipFilePK13 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 2018, 112804);
        ipFilePK14 = new IpFilePK(FILE_SEQ, FILE_TYP_PATENT, 1979, 44710);

        if (!dataLoaded) {
            // Marks
            indexService.delete(IpMark.class);
            indexService.index(ipFilePK0, IpMark.class);
            indexService.index(ipFilePK1, IpMark.class);
            indexService.index(ipFilePK2, IpMark.class);
            indexService.index(ipFilePK3, IpMark.class);
            indexService.index(ipFilePK4, IpMark.class);
            indexService.index(ipFilePK5, IpMark.class);
            indexService.index(ipFilePK6, IpMark.class);
            indexService.index(ipFilePK7, IpMark.class);
            indexService.index(ipFilePK8, IpMark.class);

            // Patents
            indexService.delete(IpPatent.class);
            indexService.index(ipFilePK9, IpPatent.class);
            indexService.index(ipFilePK10, IpPatent.class);
            indexService.index(ipFilePK11, IpPatent.class);
            indexService.index(ipFilePK12, IpPatent.class);
            indexService.index(ipFilePK13, IpPatent.class);
            indexService.index(ipFilePK14, IpPatent.class);

            dataLoaded = true;
        }
    }

    @Test
    public void testSearchMarkByFileNumber() {
        IpMark ipMark = ipMarkRepository.findById(ipFilePK8).orElse(null);
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .fileNbr(ipFilePK8.getFileNbr().toString());

        Page<AutocompleteIpoSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());

        AutocompleteIpoSearchResult searchResult = result.getContent().get(0);
        assertEquals(FILE_SEQ, searchResult.getPk().getFileSeq());
        assertEquals("D", searchResult.getPk().getFileType());
        assertEquals(FILE_SER_MARK, searchResult.getPk().getFileSer().longValue());
        assertEquals(135920L, searchResult.getPk().getFileNbr().longValue());
        assertEquals("GOLDEN LINE", searchResult.getTitle());
        assertTrue(searchResult.getFilingDate().equals(ipMark.getFilingDate()));
        assertTrue(searchResult.getRegistrationDate().equals(ipMark.getRegistrationDate()));
        assertEquals("93768", searchResult.getRegistrationNbr().toString());
        assertEquals("A", searchResult.getRegistrationDup());
        assertTrue(searchResult.getEntitlementDate().equals(ipMark.getEntitlementDate()));
        assertTrue(searchResult.getExpirationDate().equals(ipMark.getExpirationDate()));
        assertEquals("D", searchResult.getSignWcode());
    }

    @Test
    public void testSearchMarkByRegistrationNumber() {
        IpMark ipMark = ipMarkRepository.findById(ipFilePK8).orElse(null);
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam();
        searchParam
                .addFileType("D").addFileType("N")
                .registrationNbr(ipMark.getRegistrationNbr().toString());

        Page<AutocompleteIpoSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
        AutocompleteIpoSearchResult searchResult = result.getContent().get(0);
        assertEquals(FILE_SEQ, searchResult.getPk().getFileSeq());
        assertEquals("D", searchResult.getPk().getFileType());
        assertEquals(FILE_SER_MARK, searchResult.getPk().getFileSer().longValue());
        assertEquals(135920L, searchResult.getPk().getFileNbr().longValue());
        assertEquals("GOLDEN LINE", searchResult.getTitle());
        assertTrue(searchResult.getFilingDate().equals(ipMark.getFilingDate()));
        assertTrue(searchResult.getRegistrationDate().equals(ipMark.getRegistrationDate()));
        assertEquals("93768", searchResult.getRegistrationNbr().toString());
        assertEquals("A", searchResult.getRegistrationDup());
        assertTrue(searchResult.getEntitlementDate().equals(ipMark.getEntitlementDate()));
        assertTrue(searchResult.getExpirationDate().equals(ipMark.getExpirationDate()));
    }


    @Test
    public void testSearchPatentByFileNumber() {
        IpPatent ipPatent = ipPatentRepository.findById(ipFilePK12).orElse(null);
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam();
        searchParam
                .addFileType("P")
                .fileNbr(ipFilePK12.getFileNbr().toString());

        Page<AutocompleteIpoSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());

        AutocompleteIpoSearchResult searchResult = result.getContent().get(0);
        assertEquals(FILE_SEQ, searchResult.getPk().getFileSeq());
        assertEquals("P", searchResult.getPk().getFileType());
        assertEquals(2014, searchResult.getPk().getFileSer().longValue());
        assertEquals(111705L, searchResult.getPk().getFileNbr().longValue());
        assertEquals("СИСТЕМА ЗА УПРАВЛЕНИЕ НА ТРИФАЗЕН БЕЗКОЛЕКТОРЕН ЕЛЕКТРОДВИГАТЕЛ", searchResult.getTitle());
        assertEquals(66765L, searchResult.getRegistrationNbr().longValue());
    }

    @Test
    public void testSearchPatentByRegistrationNumber() {
        IpPatent ipPatent = ipPatentRepository.findById(ipFilePK12).orElse(null);
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam();
        searchParam
                .addFileType("P")
                .registrationNbr(ipPatent.getRegistrationNbr().toString());

        Page<AutocompleteIpoSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());

        AutocompleteIpoSearchResult searchResult = result.getContent().get(0);
        assertEquals(FILE_SEQ, searchResult.getPk().getFileSeq());
        assertEquals("P", searchResult.getPk().getFileType());
        assertEquals(2014, searchResult.getPk().getFileSer().longValue());
        assertEquals(111705L, searchResult.getPk().getFileNbr().longValue());
        assertEquals("СИСТЕМА ЗА УПРАВЛЕНИЕ НА ТРИФАЗЕН БЕЗКОЛЕКТОРЕН ЕЛЕКТРОДВИГАТЕЛ", searchResult.getTitle());
        assertEquals(66765L, searchResult.getRegistrationNbr().longValue());
    }

    @Test
    public void testSearchPatentByFileNumberWildcard() {
        IpPatent ipPatent = ipPatentRepository.findById(ipFilePK12).orElse(null);
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam();
        searchParam
                .addFileType("P")
                .fileNbr("0");

        Page<AutocompleteIpoSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByRegistrationNumberWildcard() {
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam();
        searchParam
                .addFileType("P")
                .registrationNbr("2");

        Page<AutocompleteIpoSearchResult> result = searchService.search(searchParam);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchPatentByPK() {
        AutocompleteIpoSearchParam searchParam = new AutocompleteIpoSearchParam();
        searchParam
                .fileSeq(FILE_SEQ)
                .addFileType("P")
                .fileSer("2016")
                .fileNbr("112320");

        Page<AutocompleteIpoSearchResult> result = searchService.search(searchParam);

        assertEquals(1, result.getTotalElements());
        assertEquals("773", result.getContent().get(0).getStatusCode());
        assertEquals(601304L, result.getContent().get(0).getProcNbr().longValue());
        assertEquals(10L, result.getContent().get(0).getProcTyp().longValue());
    }
}
