package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.core.service.search.IndexCountService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.mark.*;
import bg.duosoft.ipas.persistence.model.entity.patent.*;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationship;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.util.search.IndexProgressMonitor;
import bg.duosoft.ipas.util.search.EntityIndexProgressMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexReader;
import org.hibernate.CacheMode;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IndexServiceImpl implements IndexService, IndexCountService {

    private static List<Class<?>> DEFAULT_HIBERNATE_SEARCH_INDEXER_CLASSES = Arrays.asList(IpPatentSummary.class, IpPatentIpcClasses.class, IpPatentCpcClasses.class, IpPatentLocarnoClasses.class, IpFileRelationship.class, IpMarkNiceClasses.class, IpLogoViennaClasses.class, IpPersonAddresses.class, IpUserdocPerson.class, IpMarkAttachmentViennaClasses.class);
    private static List<Class<?>> EXTENDED_HIBERNATE_SEARCH_INDEXER_CLASSES = Arrays.asList(IpAction.class, IpProc.class, IpUserdoc.class, IpDoc.class, IpMark.class, IpPatent.class);


    @Autowired
    private HibernateSearchService hibernateSearchService;

    @Autowired
    private Manualndexer manualndexer;


    public IndexProgressMonitor indexAll(boolean async) {
        List<Class<?>> allClasses = getAllClasses();
        return _index(async, allClasses.toArray(new Class<?>[allClasses.size()]));
    }

    public void deleteAll() {
        List<Class<?>> cls = getAllClasses();
        _delete(cls.toArray(new Class<?>[cls.size()]));
    }
    private List<Class<?>> getAllClasses() {
        List<Class<?>> allClasses = new ArrayList<Class<?>>();
        allClasses.addAll(DEFAULT_HIBERNATE_SEARCH_INDEXER_CLASSES);
        allClasses.addAll(EXTENDED_HIBERNATE_SEARCH_INDEXER_CLASSES);
        return allClasses;
    }

    private IndexProgressMonitor _index(boolean async, Class<?>... entityTypes) {
        IndexProgressMonitor monitor = IndexProgressMonitor.initIndexProgressMonitor();
        //adding all entity monitors
        Map<Class<?>, EntityIndexProgressMonitor> entityMonitors = Arrays.stream(entityTypes).collect(Collectors.toMap(r -> r, r -> monitor.addProgressMonitor(r)));
        for (Class<?> entityType : entityTypes) {
            EntityIndexProgressMonitor m = entityMonitors.get(entityType);
            if (DEFAULT_HIBERNATE_SEARCH_INDEXER_CLASSES.contains(entityType)) {
                _startDefaultMassIndexer(entityType, m, async);
            } else if (EXTENDED_HIBERNATE_SEARCH_INDEXER_CLASSES.contains(entityType)) {
                _startManualMassIndexer(entityType, m, async);
            } else {
                throw new RuntimeException("Unsupported entityType: " + entityType);
            }
        }
        return monitor;
    }

    private void _startDefaultMassIndexer(Class<?> entityType, MassIndexerProgressMonitor progressMonitor, boolean async) {
        MassIndexer ind = hibernateSearchService.getFullTextEntityManager()
                .createIndexer(entityType)
                .batchSizeToLoadObjects(25)
                .cacheMode(CacheMode.NORMAL)
                .threadsToLoadObjects(async ? 1 : 5)
                .idFetchSize(50)
                .progressMonitor(progressMonitor);
        if (async) {
            ind.start();
        } else {
            try {
                ind.startAndWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void _startManualMassIndexer(Class<?> entityType, EntityIndexProgressMonitor progressMonitor, boolean async) {
        manualndexer.index(entityType, progressMonitor, async);
    }

    public <ID extends Serializable, T> void index(ID idObj, Class<T> entityType) {
        FullTextSession fullTextSession = hibernateSearchService.getFullTextSession();
        try {
            Transaction tx = fullTextSession.beginTransaction();
            try {
                T t = fullTextSession.load(entityType, idObj);
                fullTextSession.index(t);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        } finally {
            fullTextSession.close();
        }
    }
    public void delete(Class<?> entityType) {
        _delete(entityType);
    }
    private void _delete(Class<?>... entityTypes) {
        FullTextSession fullTextSession = hibernateSearchService.getFullTextSession();
        try {
            Transaction tx = fullTextSession.beginTransaction();
            try {
                for (Class<?> entityType : entityTypes) {
                    fullTextSession.purgeAll(entityType);
                }
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        } finally {
            fullTextSession.close();
        }
    }

    public <ID extends Serializable, T> void delete(ID idObj, Class<T> entityType) {
        FullTextSession fullTextSession = hibernateSearchService.getFullTextSession();
        try {
            Transaction tx = fullTextSession.beginTransaction();
            try {
                fullTextSession.purge(entityType, idObj);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        } finally {
            fullTextSession.close();
        }
    }

    public Integer count(Class<?> entityType) {

        FullTextSession fullTextSession = hibernateSearchService.getFullTextSession();

        try {
            SearchFactory searchFactory = hibernateSearchService.getFullTextSession().getSearchFactory();
            IndexReader reader = searchFactory.getIndexReaderAccessor().open(entityType);

            return reader.numDocs();
        } finally {
            fullTextSession.close();
        }
    }

    public IndexProgressMonitor indexMarks() {
        return _index(true, IpMark.class);
    }

    public Integer countMarks() {
        return count(IpMark.class);
    }

    public IndexProgressMonitor indexPatents() {
        return _index(true, IpPatent.class);
    }

    public Integer countPatents() {
        return count(IpPatent.class);
    }

    public IndexProgressMonitor indexPatentLocarnoClasses() {
        return _index(true, IpPatentLocarnoClasses.class);
    }

    public Integer countPatentLocarnoClasses() {
        return count(IpPatentLocarnoClasses.class);
    }

    public IndexProgressMonitor indexFileRelationships() {
        return _index(true, IpFileRelationship.class);
    }

    public Integer countFileRelationships() {
        return count(IpFileRelationship.class);
    }

    public IndexProgressMonitor indexActions() {
        return _index(true, IpAction.class);
    }

    public Integer countActions() {
        return count(IpAction.class);
    }

    public IndexProgressMonitor indexProcesses() {
        return _index(true, IpProc.class);
    }

    public Integer countProcesses() {
        return count(IpProc.class);
    }

    public IndexProgressMonitor indexPersonAddresses() {
        return _index(true, IpPersonAddresses.class);
    }

    public Integer countPersonAddresses() {
        return count(IpPersonAddresses.class);
    }

    public IndexProgressMonitor indexPatentSummary() {
        return _index(true, IpPatentSummary.class);
    }

    public Integer countPatentSummary() {
        return count(IpPatentSummary.class);
    }

    public IndexProgressMonitor indexIpcClasses() {
        return _index(true, IpPatentIpcClasses.class);
    }

    @Override
    public IndexProgressMonitor indexCpcClasses() {
        return _index(true, IpPatentCpcClasses.class);
    }

    public Integer countIpcClasses() {
        return count(IpPatentIpcClasses.class);
    }

    @Override
    public Integer countCpcClasses() {
        return count(IpPatentCpcClasses.class);
    }

    public IndexProgressMonitor indexNiceClasses() {
        return _index(true, IpMarkNiceClasses.class);
    }

    public Integer countNiceClasses() {
        return count(IpMarkNiceClasses.class);
    }

    public IndexProgressMonitor indexViennaClasses() {
        return _index(true, IpLogoViennaClasses.class);
    }
    public IndexProgressMonitor indexMarkAttachmentViennaClasses() {
        return _index(true, IpMarkAttachmentViennaClasses.class);
    }

    public Integer countViennaClasses() {
        return count(IpLogoViennaClasses.class);
    }

    public Integer countMarkAttachmentViennaClasses() {
        return count(IpMarkAttachmentViennaClasses.class);
    }

    public IndexProgressMonitor indexDocs() {
        return _index(true, IpDoc.class);
    }

    public Integer countDocs() {
        return count(IpDoc.class);
    }

    public IndexProgressMonitor indexUserDocs() {
        return _index(true, IpUserdoc.class);
    }

    public Integer countUserDocs() {
        return count(IpUserdoc.class);
    }

    public IndexProgressMonitor indexUserDocPersons() {
        return _index(true, IpUserdocPerson.class);
    }

    public Integer countUserDocPersons() {
        return count(IpUserdocPerson.class);
    }

}
