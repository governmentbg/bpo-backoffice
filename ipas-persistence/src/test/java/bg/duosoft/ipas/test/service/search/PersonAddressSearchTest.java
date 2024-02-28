package bg.duosoft.ipas.test.service.search;

import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.core.model.search.CCriteriaPerson;
import bg.duosoft.ipas.core.model.util.PersonAddressResult;
import bg.duosoft.ipas.core.service.impl.search.PersonAddressSearchServiceImpl;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.PersonAddressSearchService;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.repository.entity.person.IpPersonAdressesRepository;
import bg.duosoft.ipas.core.model.search.SearchPage;
import bg.duosoft.ipas.util.search.SearchUtil;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.hibernate.CacheMode;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
public class PersonAddressSearchTest extends TestBase {

    @Autowired
    private IndexService indexService;

    @Autowired
    private PersonAddressSearchService personAddressSearchService;

    @Autowired
    private PersonAddressSearchServiceImpl personAddressSearchServiceImpl;

    @Autowired
    private IpPersonAdressesRepository ipPersonAdressesRepository;

    @Autowired
    private PatentService patentService;

    @PersistenceContext
    private EntityManager em;

    private long getSize(Query query)  {
        SearchPage searchPage = new CCriteriaPerson();

        Page<PersonAddressResult> page = personAddressSearchServiceImpl.search(query, searchPage);

        return page.getTotalElements();
    }

    @Before
    public void setUp() {

        Term term = new Term(SearchUtil.FIELD_IP_PERSON_PERSON_NBR, "*");
        Query query = new WildcardQuery(term);

        long size = getSize(query);
        long all = ipPersonAdressesRepository.count();
        if (size != all || size == 0) {

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
        }
        //indexService.index(new IpPersonAddressesPK(380503, 1), IpPersonAddresses.class);
        //indexService.index(new IpPersonAddressesPK(380503, 2), IpPersonAddresses.class);
    }

    @After
    public void tearDown() {
        //searchService.deleteAll();
    }

    @Test
    public void testSearchByPersonName() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonName("Петър Атанасов");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonName1() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonName("\"СОФАРМА\" АД");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(20, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("Петър".toLowerCase());

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(2113, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords1() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("СОФАРМ".toLowerCase());

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(65, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords2() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("Петър Атанасов");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(68, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords3() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("\"СОФАРМА\" АД");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(63, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords4() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("Giesecke+Devrient Currency Technology GmbH");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(39, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords5() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("\"ПРЕСТИЖ-96\" АД");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(33, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords6() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("TOD'S S.P.A.");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(13, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords8() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("Allergan, Inc.");
        cCriteriaPerson.setPageSize(20);

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(14, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords9() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("SWATCH AG (SWATCH SA)(SWATCH LTD.)");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(17, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords10() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("Johnson & Johnson");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(56, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords11() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("Boehringer Ingelheim Pharma GmbH & Co. KG/  Boehringer Ingelheim International GmbH");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords12() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("***");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(12, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameContainsWords13() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("Термопласт");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(6, result.getTotalElements());
    }

    @Test
    public void testSearchByNationalityCountryCode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setNationalityCountryCode("UG");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchByResidenceCountryCode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setResidenceCountryCode("UZ");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchByEmail() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setEmail("bobi_kostadinova@abv.bg");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchByEmail2() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setEmail("office@patentuni.com");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(3, result.getTotalElements());
    }

    @Test
    public void testSearchByEmailContainsWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setEmailContainsWords("bobi_kostadinova");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchByEmailContainsWords2() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setEmailContainsWords("office@patentuni.com");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(5, result.getTotalElements());
    }

    @Test
    public void testSearchByEmailContainsWords3() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setEmailContainsWords("office");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(940, result.getTotalElements());
    }

    @Test
    public void testSearchByTelephoneContainsWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setTelephoneContainsWords("058603125");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(33, result.getTotalElements());
    }

    @Test
    public void testSearchByTelephoneContainsWords1() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setTelephoneContainsWords("082 810 11");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(8, result.getTotalElements());
    }

    @Test
    public void testSearchByCity() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setCity("Луковит");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(69, result.getTotalElements());
    }

    @Test
    public void testSearchByCityContainsWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setCityContainsWords("загора");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(3168, result.getTotalElements());
    }

    @Test
    public void testSearchByStreet() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setStreet("ул. \"Бели Лом\" 37");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByStreetExpectZero() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setStreet("бели");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testSearchByStreetContainsWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setStreetContainsWords("бели");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(357, result.getTotalElements());
    }

    @Test
    public void testSearchByStreetContainsWords1() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setStreetContainsWords("ул.\"Илиенско шосе\" 16");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(4, result.getTotalElements());
    }

    @Test
    public void testSearchByStreetContainsWords2() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setStreetContainsWords("ул.\"Илиенско шосе\" 1");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(4, result.getTotalElements());
    }

    @Test
    public void testSearchByAgentCode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setAgentCode("11");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(11, result.getTotalElements());
    }
    @Test
    public void testSearchByInactiveAgentCode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setAgentCode("673");
        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);
        assertEquals(1, result.getTotalElements());
        assertEquals(true, result.getContent().get(0).getAgentIndInactive());
    }

    @Test
    public void testSearchByAgentCodeOrNameContainsWords_AgentCode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setAgentCodeOrNameContainsWords("25");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(11, result.getTotalElements());
    }

    @Test
    public void testSearchByAgentCodeOrNameContainsWords_NameContainsWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setAgentCodeOrNameContainsWords("Валентина Великова Нешева");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByOnlyAgent() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setOnlyAgent(true);

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(714, result.getTotalElements());
        for (PersonAddressResult personAddressResult:result.getContent()) {
            assertTrue(!personAddressResult.getAgentCode().isEmpty());
        }
    }

    @Test
    public void testSearchByOnlyForeignCitizens() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setOnlyForeignCitizens(true);

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(159675, result.getTotalElements());
        for (PersonAddressResult personAddressResult:result.getContent()) {
            assertTrue(!personAddressResult.getNationalityCountryCode().equalsIgnoreCase(SearchUtil.BG_CODE));
        }
    }

    @Test
    public void testSearchResult() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonName("Валентина Великова Нешева");
        cCriteriaPerson.setAgentCode("25");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(1, result.getTotalElements());
        List<PersonAddressResult> content = result.getContent();
        PersonAddressResult addressResult = content.get(0);
        assertEquals(111092L, addressResult.getPersonNbr().longValue());
        assertEquals(1L, addressResult.getAddressNbr().longValue());
        assertEquals("Валентина Великова Нешева", addressResult.getPersonName());
        assertEquals("BG", addressResult.getNationalityCountryCode());
        assertEquals(false, addressResult.getIndCompany());
        assertEquals("25", addressResult.getAgentCode());
        assertEquals("София", addressResult.getCityName());
        assertEquals("1000 София, ул. „Солунска“ 45, ет. 3, ап. 14", addressResult.getAddressZone());
        assertEquals("ул. „Солунска“ 45, ет. 3, ап. 14", addressResult.getAddressStreet());
        assertEquals("1000", addressResult.getZipCode());
        assertEquals("office@patentuni.com; nesheva@patentuni.com", addressResult.getEmail());
        assertEquals("+359 952 51 30; +359 888 315 093", addressResult.getTelephone());
        assertEquals("BG", addressResult.getResidenceCountryCode());
    }

    @Test
    public void testSearchByPartnershipCode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setAgentCode("A1");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByAgentCodeOrNameContainsWords_PartershipPersonName() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setAgentCodeOrNameContainsWords("ПЕТОШЕВИЧ България");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testSearchByPeronNameAndZipcode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("иван");
        cCriteriaPerson.setZipCode("2760");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchByPeronNameAndPartOfZipcode() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("иван");
        cCriteriaPerson.setZipCode("276");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testSearchByOnlyActiveAgent() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setOnlyAgent(true);
        cCriteriaPerson.setOnlyActiveAgent(true);

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(653, result.getTotalElements());
        for (PersonAddressResult personAddressResult:result.getContent()) {
            assertTrue(!personAddressResult.getAgentCode().isEmpty());
        }
    }

    @Test
    public void testSearchByOnlyActivePartnership() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setAgentCode("A1");
        cCriteriaPerson.setOnlyAgent(true);
        cCriteriaPerson.setOnlyActiveAgent(true);

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(1, result.getTotalElements());
        for (PersonAddressResult personAddressResult:result.getContent()) {
            assertTrue(!personAddressResult.getAgentCode().isEmpty());
        }
    }

    @Test
    public void testSearchByPersonNameContainsWord2() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("ябъл");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(5, result.getTotalElements());

    }

    @Test
    public void testSearchByPersonNameContainsWord3() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("груп бъл");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(96, result.getTotalElements());

    }

    @Test
    public void testSearchByPersonNameWholeWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameWholeWords("БЪЛГАРСКА АСОЦИАЦИЯ");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(121, result.getTotalElements());

    }

    @Test
    public void testSearchByPersonNameWholeWords_PartialWords() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameWholeWords("ЪЛГАРСКА АСОЦИАЦИ");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(0, result.getTotalElements());

    }

    @Test
    public void testSearchByPersonNameExactly() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameExactly("СОЦИАЦИЯ БЪЛ");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(11, result.getTotalElements());

    }

    @Test
    public void testSearchByPersonNameExactly1() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameExactly("СОЦИАЦИ БЪЛ");

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(0, result.getTotalElements());

    }

    @Test
    public void testSearchByPersonNameExactlyHasGral() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson
                .setPersonNameExactly("\"ален мак\" еад".toLowerCase());

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(4, result.getTotalElements());
    }

    @Test
    public void testSearchByPersonNameExactlyExcludeOldVersions () {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson
                .setPersonNameExactly("\"ален мак\" еад".toLowerCase());
        cCriteriaPerson
                .setExcludeOldVersions(true);

        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(3, result.getTotalElements());
    }
    @Test
    public void testSearchByIndCompanyTrue() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setIndCompany(true);
        cCriteriaPerson.setPersonNameContainsWords("соф");
        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(924, result.getTotalElements());
    }

    @Test
    public void testSearchByIndCompanyFalse() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setIndCompany(false);
        cCriteriaPerson.setPersonNameContainsWords("соф");
        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);

        assertEquals(202, result.getTotalElements());
    }
    public void testSearchByIndCompanyNull() {
        CCriteriaPerson cCriteriaPerson = new CCriteriaPerson();
        cCriteriaPerson.setPersonNameContainsWords("соф");
        Page<PersonAddressResult> result = personAddressSearchService.search(cCriteriaPerson);
        assertEquals(1126, result.getTotalElements());
    }
}
