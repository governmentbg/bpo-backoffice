package bg.duosoft.ipas.test.service.search;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.service.search.IndexCountService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpLogoViennaClasses;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.model.entity.person.IpPerson;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddressesPK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.repository.entity.mark.IpMarkRepository;
import bg.duosoft.ipas.persistence.repository.entity.process.IpProcRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.search.SearchUtil;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Rollback
@Transactional
public class IndexServiceTest extends TestBase {
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
    private IpPatentSummaryPK ipPatentSummaryPK;

    private IpProcPK ipProcPK1;
    private IpProcPK ipProcPK2;
    private IpProcPK ipProcPK3;
    private IpProcPK ipProcPK4;

    private IpPatentIpcClassesPK ipPatentIpcClassesPK1;

    private static boolean dataLoaded = false;
// 944945 2
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

            // Mark vienna classes
            for (IpLogoViennaClasses viennaClasses : ipMark.getLogo().getIpLogoViennaClassesCollection()) {
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

            dataLoaded = true;
        }
    }


    @Test
    public void searchActionByNewStatusCode() {
        FullTextSession fullTextSession = getFullTextSession();
        Transaction transaction = fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = getQueryBuilder(IpAction.class);
        Query query = queryBuilder
                .keyword()
                .onField("newStatusCode.pk.statusCode")
                .matching("185")
                .createQuery();

        FullTextQuery jpaQuery = getFullTextQuery(query, IpAction.class);

        assertEquals(2, jpaQuery.getResultSize());

        transaction.commit();
        fullTextSession.close();
    }


    @Test
    public void searchActionByPriorStatusCode() {
        FullTextSession fullTextSession = getFullTextSession();
        Transaction transaction = fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = getQueryBuilder(IpAction.class);
        Query query = queryBuilder
                .keyword()
                .onField("priorStatusCode.pk.statusCode")
                .matching("185")
                .createQuery();

        FullTextQuery jpaQuery = getFullTextQuery(query, IpAction.class);

        assertEquals(2, jpaQuery.getResultSize());

        transaction.commit();
        fullTextSession.close();
    }


    @Test
    public void searchActionByPriorStatusCodeDateRange() throws ParseException {
        FullTextSession fullTextSession = getFullTextSession();
        Transaction transaction = fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = getQueryBuilder(IpAction.class);
        BooleanJunction<BooleanJunction> query = queryBuilder.bool();

        Query queryStatusCode = queryBuilder
                .keyword()
                .onField("priorStatusCode.pk.statusCode")
                .matching("185")
                .createQuery();
        query.must(queryStatusCode);

        Date dt = new SimpleDateFormat("yyyyMMdd").parse("20180920");
        Query queryDateRange = SearchUtil.getQueryRange(queryBuilder,"priorStatusDate", dt, dt);
        query.must(queryDateRange);

        FullTextQuery jpaQuery = getFullTextQuery(query.createQuery(), IpAction.class);

        assertEquals(1, jpaQuery.getResultSize());

        transaction.commit();
        fullTextSession.close();
    }

    @Test
    public void searchFirstActionByStatusCodeDateRange() throws ParseException {
        FullTextSession fullTextSession = getFullTextSession();
        Transaction transaction = fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = getQueryBuilder(IpAction.class);
        BooleanJunction<BooleanJunction> query = queryBuilder.bool();

        Query queryStatusCode = queryBuilder
                .keyword()
                .onField("priorStatusCode.pk.statusCode")
                .matching("183")
                .createQuery();
        query.must(queryStatusCode);

        Date dt = new SimpleDateFormat("yyyyMMdd").parse("20180919");
        Query queryDateRange = SearchUtil.getQueryRange(queryBuilder,"priorStatusDate", dt, dt);
        query.must(queryDateRange);

        FullTextQuery jpaQuery = getFullTextQuery(query.createQuery(), IpAction.class);

        assertEquals(1, jpaQuery.getResultSize());

        transaction.commit();
        fullTextSession.close();
    }

    @Test
    public void searchLastActionByStatusCodeDateRange() throws ParseException {
        FullTextSession fullTextSession = getFullTextSession();
        Transaction transaction = fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = getQueryBuilder(IpAction.class);

        Date dt = new SimpleDateFormat("yyyyMMdd").parse("20181029");
        BooleanJunction<BooleanJunction> query = queryBuilder.bool();

        // prior status code and prior date
        BooleanJunction<BooleanJunction> queryPrior = queryBuilder.bool();
        Query queryPriorStatusCode = queryBuilder
                .keyword()
                .onField("priorStatusCode.pk.statusCode")
                .matching("200")
                .createQuery();
        queryPrior.must(queryPriorStatusCode);
        Query queryPriorDateRange = SearchUtil.getQueryRange(queryBuilder,"priorStatusDate", dt, dt);
        queryPrior.must(queryPriorDateRange);
        query.should(queryPrior.createQuery());

        // new status code and action date
        BooleanJunction<BooleanJunction> queryNew = queryBuilder.bool();
        Query queryNewStatusCode = queryBuilder
                .keyword()
                .onField("newStatusCode.pk.statusCode")
                .matching("200")
                .createQuery();
        queryNew.must(queryNewStatusCode);
        Query queryNewDateRange = SearchUtil.getQueryRange(queryBuilder,"actionDate", dt, dt);
        queryNew.must(queryNewDateRange);
        query.should(queryNew.createQuery());

        FullTextQuery jpaQuery = getFullTextQuery(query.createQuery(), IpAction.class);

        assertEquals(1, jpaQuery.getResultSize());

        transaction.commit();
        fullTextSession.close();
    }

    @Test
    public void searchProcessByStatusCode() {
        FullTextSession fullTextSession = getFullTextSession();
        Transaction transaction = fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = getQueryBuilder(IpProc.class);
        Query query = queryBuilder
                .keyword()
                .onField("statusCode.pk.statusCode")
                .matching("237")
                .createQuery();

        FullTextQuery jpaQuery = getFullTextQuery(query, IpProc.class);

        assertEquals(1, jpaQuery.getResultSize());

        transaction.commit();
        fullTextSession.close();
    }



    @Test
    public void searchProcessByStatusCode1() {
        int count = ((IndexCountService)indexService).countMarks();
        assertEquals(9, count);

        ipFilePK8 = new IpFilePK(FILE_SEQ, "N", FILE_SER_MARK, 149893);
        indexService.index(ipFilePK8, IpMark.class);

        count = ((IndexCountService)indexService).countMarks();
        assertEquals(10, count);

        indexService.delete(ipFilePK8, IpMark.class);
        count = ((IndexCountService)indexService).countMarks();
        assertEquals(9, count);
    }


    public QueryBuilder getQueryBuilder(Class<?> entityType) {

        return getFullTextEntityManager().getSearchFactory()
                .buildQueryBuilder()
                .forEntity(entityType)
                .get();
    }

    public FullTextSession getFullTextSession() {
        Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();

        return org.hibernate.search.Search.getFullTextSession(session);
    }

    public FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... entityTypes) {
        return getFullTextEntityManager().createFullTextQuery(luceneQuery, entityTypes);
    }

    public FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(em);
    }
}
