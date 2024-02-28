package bg.duosoft.ipas.test.service.search;

import bg.duosoft.ipas.core.model.search.CSearchParam;
import bg.duosoft.ipas.core.model.search.CSearchResult;
import bg.duosoft.ipas.core.service.impl.search.IpoSearchServiceImpl;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.enums.search.SearchOperatorTextType;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Transactional
public class HibernateSearchServiceTest extends TestBase {

    private final static String FILE_SEQ = "BG";
    private final static String FILE_TYP = "P";
    private final static int FILE_SER = 2018;
    private final static String IP_FILE_PK0_TITLE = "КОРЕЛИРАНО МНОГОКРАТНО СЕМПЛИРАНЕ РЕД";

    @Autowired
    private IpoSearchServiceImpl ipoSearchService;

    @Autowired
    private IndexService indexService;

    private IpFilePK ipFilePK0;
    private IpFilePK ipFilePK1;
    private IpFilePK ipFilePK2;
    private IpFilePK ipFilePK3;
    private IpFilePK ipFilePK4;
    private IpFilePK ipFilePK5;
    private IpFilePK ipFilePK6;

    @Before
    public void setUp() {
        indexService.delete(IpPatent.class);
        ipFilePK0 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112661);
        indexService.index(ipFilePK0, IpPatent.class);

        ipFilePK1 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112662);
        indexService.index(ipFilePK1, IpPatent.class);

        ipFilePK2 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112663);
        indexService.index(ipFilePK2, IpPatent.class);

        ipFilePK3 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112664);
        indexService.index(ipFilePK3, IpPatent.class);

        ipFilePK4 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112665);
        indexService.index(ipFilePK4, IpPatent.class);

        ipFilePK5 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112666);
        indexService.index(ipFilePK5, IpPatent.class);

        ipFilePK6 = new IpFilePK(FILE_SEQ, FILE_TYP, FILE_SER, 112670);
    }

    @After
    public void tearDown() {
        //patentSearchService.deleteAll();
    }

    @Test
    public void indexById() {

        indexService.index(ipFilePK6, IpPatent.class);

        CSearchParam searchParam = new CSearchParam();
        searchParam.setFileSeq(ipFilePK6.getFileSeq());

        List<String> filesTypes = new ArrayList<>();
        filesTypes.add(ipFilePK6.getFileTyp());

        searchParam.setFileTypes(filesTypes);
        searchParam.setFromFileSer("" + ipFilePK6.getFileSer());
        searchParam.setToFileSer("" + ipFilePK6.getFileSer());
        searchParam.setFromFileNbr("" + ipFilePK6.getFileNbr());
        searchParam.setToFileNbr("" + ipFilePK6.getFileNbr());

        long size = ipoSearchService.search(searchParam).getTotalElements();
        assertEquals(1, size);
    }

    @Test
    public void deleteAll() {
        indexService.delete(IpPatent.class);

        CSearchParam searchParam = new CSearchParam();
        List<String> filesTypes = new ArrayList<>();
        filesTypes.add(FILE_TYP);

        searchParam.setFileTypes(filesTypes);
        long size = ipoSearchService.search(searchParam).getTotalElements();
        assertEquals(0, size);
    }

    @Test
    public void delete() {
        indexService.delete(IpPatent.class);

        CSearchParam searchParam = new CSearchParam();
        List<String> filesTypes = new ArrayList<>();
        filesTypes.add(FILE_TYP);

        searchParam.setFileTypes(filesTypes);
        long size = ipoSearchService.search(searchParam).getTotalElements();
        assertEquals(0, size);
    }

    @Test
    public void deleteById() {
        indexService.delete(IpMark.class);
        indexService.delete(ipFilePK5, IpPatent.class);

        CSearchParam searchParam = new CSearchParam();
        List<String> filesTypes = new ArrayList<>();
        filesTypes.add(FILE_TYP);

        searchParam.setFileTypes(filesTypes);
        long size = ipoSearchService.search(searchParam).getTotalElements();
        assertEquals(5, size);
    }

    @Test
    public void getResultListBySearchParam() {
        CSearchParam searchParam = new CSearchParam();
        searchParam
                .title(IP_FILE_PK0_TITLE.toLowerCase())
                .titleTypeSearch(SearchOperatorTextType.KEYWORDS);
        List<String> filesTypes = new ArrayList<>();
        filesTypes.add(FILE_TYP);

        searchParam.setFileTypes(filesTypes);

        Page<CSearchResult> page =
                ipoSearchService.search(searchParam);

        assertEquals(1, page.getTotalElements());
    }

    private Query getTermQuery(String field, String queryStr) {
        Term term = new Term(field, queryStr);
        return new TermQuery(term);
    }
}