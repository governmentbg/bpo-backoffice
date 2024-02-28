package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.core.mapper.search.*;
import bg.duosoft.ipas.persistence.model.entity.action.IpAction;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.persistence.model.entity.vw.ind.*;
import bg.duosoft.ipas.util.search.EntityIndexProgressMonitor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.event.spi.EventSource;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.backend.spi.Work;
import org.hibernate.search.backend.spi.WorkType;
import org.hibernate.search.backend.spi.Worker;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.event.impl.EventSourceTransactionContext;
import org.hibernate.search.hcore.util.impl.ContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * User: ggeorgiev
 * Date: 11.03.2021
 * Time: 16:56
 */
@Slf4j
@Service
public class Manualndexer {
    @Autowired
    private ActionIndexMapper actionIndexMapper;
    @Autowired
    private UserdocIndexMapper userdocIndexMapper;
    @Autowired
    private ProcessIndexMapper processIndexMapper;
    @Autowired
    private DocIndexMapper docIndexMapper;
    @Autowired
    private MarkIndexMapper markIndexMapper;
    @Autowired
    private PatentIndexMapper patentIndexMapper;

    @AllArgsConstructor
    private static class ManualIndexer<E, V extends VwIndex> {
        private Class<E> entity;
        private Class<V> view;
        private IndexMapper<E, V> indexMapper;
    }
    private Map<Class, ManualIndexer> MANUAL_INDEX_CONFIGURATION = new HashMap<>();
    @PostConstruct
    private void init() {
        MANUAL_INDEX_CONFIGURATION.put(IpAction.class, new ManualIndexer(IpAction.class, VwActionIndex.class, actionIndexMapper));
        MANUAL_INDEX_CONFIGURATION.put(IpUserdoc.class, new ManualIndexer(IpUserdoc.class, VwUserdocIndex.class, userdocIndexMapper));
        MANUAL_INDEX_CONFIGURATION.put(IpProc.class, new ManualIndexer(IpProc.class, VwProcessIndex.class, processIndexMapper));
        MANUAL_INDEX_CONFIGURATION.put(IpDoc.class, new ManualIndexer(IpDoc.class, VwDocIndex.class, docIndexMapper));
        MANUAL_INDEX_CONFIGURATION.put(IpMark.class, new ManualIndexer(IpMark.class, VwMarkIndex.class, markIndexMapper));
        MANUAL_INDEX_CONFIGURATION.put(IpPatent.class, new ManualIndexer(IpPatent.class, VwPatentIndex.class, patentIndexMapper));
    }


    @Autowired
    private HibernateSearchService hibernateSearchService;
    public void index(Class<?> cls, EntityIndexProgressMonitor progressMonitor, boolean async) {
        ManualIndexer config = MANUAL_INDEX_CONFIGURATION.get(cls);
        if (config == null) {
            throw new RuntimeException("No configuration for class: "  + cls);
        }
        manualIndex(config.view, config.entity, config.indexMapper::toEntity, progressMonitor, async);

    }

    public <ID extends Serializable, T> void index(ID idObj, Class<T> entityType) {
        ManualIndexer config = MANUAL_INDEX_CONFIGURATION.get(entityType);
        if (config == null) {
            throw new RuntimeException("No configuration for class: "  + entityType);
        }
        manualIndexSingleObject(idObj, config.view, config.indexMapper::toEntity);
    }
    private <ID extends Serializable, T extends VwIndex, V> void manualIndexSingleObject(ID idObj, Class<T> viewType, Function<T, V> viewToEntityTransformer) {
        log.info("trying to index single record");

        Session session = hibernateSearchService.getEntityManagerFactory().unwrap(SessionFactory.class).openSession();
        FullTextSession fullTextSession = org.hibernate.search.Search.getFullTextSession(session);
        Transaction tx = fullTextSession.beginTransaction();
        log.info("Starting");

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(viewType);
        Root<T> rootEntry = cq.from(viewType);
        CriteriaQuery<T> singleCriteriaQuery = cq.select(rootEntry).where(cb.equal(rootEntry.get("pk"), idObj));
        TypedQuery<T> allQuery = session.createQuery(singleCriteriaQuery);
        T rec = allQuery.getSingleResult();

        Object e = viewToEntityTransformer.apply(rec);

        _index(e, idObj, fullTextSession, session);


        log.info("flushing...");
        fullTextSession.flushToIndexes();
        log.info("committing...");
        tx.commit();



//        manualIndex(config.view, config.entity, config.indexMapper::toEntity, null);

    }

    private <T> void _index(T entity, Serializable pk, FullTextSession fullTextSession, Session session) {
        String tenantIdentifier = fullTextSession.getTenantIdentifier();
        ExtendedSearchIntegrator extendedIntegrator = ContextHelper.getSearchIntegrator(session);
        EventSourceTransactionContext transactionContext = new EventSourceTransactionContext((EventSource) session);
        Worker worker = extendedIntegrator.getWorker();
        Work work = new Work(tenantIdentifier, entity, pk, WorkType.INDEX);
        worker.performWork(work, transactionContext);
    }

    private synchronized <T extends VwIndex, V> void manualIndex(Class<T> vwClass, Class<V> entityClass, Function<T, V> viewToEntityTransformer, EntityIndexProgressMonitor progressMonitor, boolean async) {
        if (progressMonitor != null) {
            progressMonitor.addToTotalCount(_countDbEntities(entityClass));
            progressMonitor.documentsAdded(0);
        }

        Runnable runnable = () -> {
            try {
                Session session = hibernateSearchService.getEntityManagerFactory().unwrap(SessionFactory.class).openSession();
                FullTextSession fullTextSession = org.hibernate.search.Search.getFullTextSession(session);
                Transaction tx = fullTextSession.beginTransaction();
                log.info(entityClass + " Purging old records");
                fullTextSession.purgeAll(entityClass);
                fullTextSession.flushToIndexes();
                log.info(entityClass + " End of purging old records");
//                String tenantIdentifier = fullTextSession.getTenantIdentifier();
//                ExtendedSearchIntegrator extendedIntegrator = ContextHelper.getSearchIntegrator(session);
//                EventSourceTransactionContext transactionContext = new EventSourceTransactionContext((EventSource) session);
//                Worker worker = extendedIntegrator.getWorker();
                log.info(entityClass + " Starting");

                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<T> cq = cb.createQuery(vwClass);
                Root<T> rootEntry = cq.from(vwClass);
                CriteriaQuery<T> all = cq.select(rootEntry);
                TypedQuery<T> allQuery = session.createQuery(all)/*.setMaxResults(1_000_000)*/;
                List<T> viewRecords = allQuery.getResultList();

                log.info(entityClass + " End of reading records. Size:" + viewRecords.size());
                int i = 0;
                for (T rec : viewRecords) {
                    i++;
                    if (i % 100_000 == 0) {
                        log.info(entityClass + " Indexing " + i + " out of " + viewRecords.size() + " ... flushing to index...");
                        fullTextSession.flushToIndexes();
                    }
                    V e = viewToEntityTransformer.apply(rec);
//                    Work work = new Work(tenantIdentifier, e, rec.getPk(), WorkType.INDEX);
//                    worker.performWork(work, transactionContext);
                    _index(e, rec.getPk(), fullTextSession, session);
                    if (progressMonitor != null) {
                        progressMonitor.documentsAdded(1);
                    }

                }
                log.info(entityClass + " flushing...");
                fullTextSession.flushToIndexes();
                log.info(entityClass + " committing...");
                tx.commit();
//                log.error("optimizing...");
//                fullTextSession.getSearchFactory().optimize(entityClass);
                log.info(entityClass + " done...");
            } catch (Exception e) {
                if (progressMonitor != null) {
                    progressMonitor.setError(ExceptionUtils.getStackTrace(e));
                }

                log.error(entityClass + " Exception...", e);
            } finally {
                if (progressMonitor != null) {
                    progressMonitor.indexingCompleted();
                }
            }
        };

        if (async) {
            Thread thread = new Thread(runnable);
            thread.start();
        } else {
            runnable.run();
        }
    }

    private long _countDbEntities(Class<?> cls) {
        EntityManager em = hibernateSearchService.getEntityManager();
        CriteriaBuilder qb = hibernateSearchService.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(cls)));
        return em.createQuery(cq).getSingleResult();
    }
}
