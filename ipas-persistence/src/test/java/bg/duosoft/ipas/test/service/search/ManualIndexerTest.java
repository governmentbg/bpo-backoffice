package bg.duosoft.ipas.test.service.search;

import bg.duosoft.ipas.core.service.impl.search.HibernateSearchService;
import bg.duosoft.ipas.core.service.impl.search.Manualndexer;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.action.IpActionPK;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.test.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * User: ggeorgiev
 * Date: 16.03.2021
 * Time: 14:14
 *
 * indexira vednuj s originalniq hibernate indexer, vednyj s prenapisaniq, koito indexira ot view i proverqva dali v dvata indexirani obekta ima ednakvi poleta
 * kato pyrvo proverqva dali v dvata obekta ima ednakvi imena na poleta (stored i non-stored), sled tova na stored poletata proverqva i stojnostite.
 */
@Slf4j
public class ManualIndexerTest extends TestBase {
    @Value("${hibernate.search.default.indexBase}")
    private String indexBase;
    @Autowired
    private Manualndexer manualndexer;
    @Autowired
    private IndexService indexService;
    @Autowired
    private HibernateSearchService hibernateSearchService;

    private static List<IpFilePK> MARKS_PKS = new ArrayList<>();
    private static List<IpFilePK> PATENT_PKS = new ArrayList<>();
    private static List<IpDocPK> USERDOC_PKS = new ArrayList<>();
    private static List<IpDocPK> DOC_PKS = new ArrayList<>();
    private static List<IpProcPK> PROC_PKS = new ArrayList<>();
    private static List<IpActionPK> ACTION_PKS = new ArrayList<>();

    static {
        MARKS_PKS.add(new IpFilePK("BG", "N", 1969, 8));
        MARKS_PKS.add(new IpFilePK("BG", "Г", 1997, 9));


        PATENT_PKS.add(new IpFilePK("BG", "С", 1997, 10));
        PATENT_PKS.add(new IpFilePK("BG", "С", 1997, 14));//with rejected denomination
        PATENT_PKS.add(new IpFilePK("BG", "S", 2015, 44));//with spcExtended fields
        PATENT_PKS.add(new IpFilePK("BG", "P", 1970, 16194));

        USERDOC_PKS.add(new IpDocPK("BG", "E", 2016, 1160151));

        DOC_PKS.add(new IpDocPK("BG", "E", 2016, 1160151));

        PROC_PKS.add(new IpProcPK("2", 51));

        ACTION_PKS.add(new IpActionPK("2", 32703, 34369));
    }
    public static String filePkToString(IpFilePK pk) {
        return Arrays.asList(pk.getFileSeq(), pk.getFileTyp(), pk.getFileSer().toString(), pk.getFileNbr().toString()).stream().collect(Collectors.joining("/"));
    }
    public static String docPkToString(IpDocPK pk) {
        return Arrays.asList(pk.getDocOri(), pk.getDocLog(), pk.getDocSer().toString(), pk.getDocNbr().toString()).stream().collect(Collectors.joining("/"));
    }
    public static String procPkToString(IpProcPK pk) {
        return pk.getProcTyp() + "/" + pk.getProcNbr();
    }

    public static String actionPkToString(IpActionPK pk) {
        return pk.getProcTyp() + "/" + pk.getProcNbr() + "/" + pk.getActionNbr();
    }
    @Test
    @Transactional
    public void testIndexMark() throws IOException {
        testIndexAndValidateObjects(MARKS_PKS, IpMark.class, r -> filePkToString((IpFilePK)r));
    }

    @Test
    @Transactional
    public void testIndexPatent() throws IOException {
        testIndexAndValidateObjects(PATENT_PKS, IpPatent.class, r -> filePkToString((IpFilePK)r));
    }
    @Test
    @Transactional
    public void testIndexUserdoc() throws IOException {
        testIndexAndValidateObjects(USERDOC_PKS, IpUserdoc.class, r -> docPkToString((IpDocPK)r));
    }
    @Test
    @Transactional
    public void testIndexDoc() throws IOException {
        testIndexAndValidateObjects(DOC_PKS, IpDoc.class, r -> docPkToString((IpDocPK)r));
    }
    @Test
    @Transactional
    public void testIndexProc() throws IOException {
        testIndexAndValidateObjects(PROC_PKS, IpProc.class, r -> procPkToString((IpProcPK) r));
    }

    @Test
    @Transactional
    public void testIndexAction() throws IOException {
        testIndexAndValidateObjects(ACTION_PKS, IpAction.class, r -> actionPkToString((IpActionPK) r));
    }
    @Test
    @Transactional
    @Ignore
    public void getFieldNames() throws IOException {
        indexService.index(PATENT_PKS.get(2), IpPatent.class);
        getAllFieldNames(IpPatent.class);
    }

    private <T extends Serializable> void testIndexAndValidateObjects(List<T> pks, Class<?> entityClass, Function<Serializable, String> pkToStringMapper) throws IOException {
        for (Serializable pk : pks) {
            //prazni indexa (ideqta e da ima samo tekushto indexiraniq zapis v indexa, za da moje spisyka ot kolonite da otgovarq na indexiraniq zapis)
            indexService.delete(entityClass);
            //indexira s originalniq hibernate code
            index(pk, entityClass);
            //prochita indexiraniq document
            Document original = getDocumentFromIndex(entityClass, pkToStringMapper.apply(pk));
            //prochita vsichki fields (vkliuchitelno i non stored)
            Set<String> originalFieldNames = getAllFieldNames(entityClass);

            //izprazva index-a za da moje da sydyrja samo novoindexiraniq element samo s negovite koloni
            indexService.delete(entityClass);
            //indexira s moq kod
            manualndexer.index(pk, entityClass);
            //prochita imenata na vsichki koloni (vkliuchitelno non stored)
            Set<String> manualFieldNames = getAllFieldNames(entityClass);

            //proverqva dali imenata na stored + non-stored kolonite sa ednakvi
            if (!CollectionUtils.isEqualCollection(originalFieldNames, manualFieldNames)) {
                log.error("Original field names : " + originalFieldNames);
                log.error("Manual field names : " + manualFieldNames);
                originalFieldNames.removeAll(manualFieldNames);
                fail("different original and manual field names. Missing field names: " + originalFieldNames);
            }

            //prochita indexiraniq s moq kod document
            Document manual = getDocumentFromIndex(entityClass, pkToStringMapper.apply(pk));

            //validira stored kolonite - imena + sydyrjanie
            validateOriginalManualDocuments(original, manual);
        }
    }

    public <ID extends Serializable, T> void index(ID idObj, Class<T> entityType) {
        FullTextSession fullTextSession = hibernateSearchService.getFullTextSession();
        Transaction tx = fullTextSession.beginTransaction();

        T t = fullTextSession.load(entityType, idObj);
        fullTextSession.index(t);

        tx.commit();
    }

    private void validateOriginalManualDocuments(Document original, Document manual) {
        assertNotNull(original);
        assertNotNull(manual);
        List<String> originalFieldNames = original.getFields().stream().map(r -> r.name()).distinct().collect(Collectors.toList());
        List<String> manualFieldNames = manual.getFields().stream().map(r -> r.name()).distinct().collect(Collectors.toList());



        if (!CollectionUtils.isEqualCollection(originalFieldNames, manualFieldNames)) {
            log.error("OriginalFieldNames:" + originalFieldNames);
            log.error("ManualFieldNames:" + manualFieldNames);
            originalFieldNames.removeAll(manualFieldNames);
            fail("different original and manual field names. Missing field names: " + originalFieldNames);
        }
        for (String field : originalFieldNames) {
            String[] originalFieldValues = original.getValues(field);
            String[] manualFieldValues = manual.getValues(field);
            assertEquals(originalFieldValues.length, manualFieldValues.length);
            assertEquals(field, Arrays.stream(originalFieldValues).sorted().collect(Collectors.toList()), Arrays.stream(manualFieldValues).sorted().collect(Collectors.toList()));
        }
    }

    //vry6ta imenata na kolonite, vkliuchitelno i not stored kolonite!
    private Set<String> getAllFieldNames(Class<?> cls) throws IOException {
        String readerDir = indexBase + cls.getName();
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File(readerDir).toPath()));
        HashSet<String> fieldnames = new HashSet<String>();
        for (LeafReaderContext subReader : reader.leaves()) {
            Fields fields = subReader.reader().fields();
            for (String fieldname : fields) {
                fieldnames.add(fieldname);
            }
        }
        return fieldnames;
    }

    private Document getDocumentFromIndex(Class<?> cls, String pk) throws IOException {
        String readerDir = indexBase + cls.getName();
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(readerDir).toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);

        TermRangeQuery range = TermRangeQuery.newStringRange("pk", pk, pk, true, true);
        TopDocs topDocs = searcher.search(range, 100);
        if (topDocs.scoreDocs == null || topDocs.scoreDocs.length != 1) {
            return null;
        } else {
            Document doc = reader.document(topDocs.scoreDocs[0].doc);
            return doc;
        }
    }
}
