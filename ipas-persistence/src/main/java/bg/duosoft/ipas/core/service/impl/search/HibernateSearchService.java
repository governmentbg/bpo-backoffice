package bg.duosoft.ipas.core.service.impl.search;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Service
@Transactional
public class HibernateSearchService {

    protected EntityManager entityManager;

    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    @Qualifier(IpasDatabaseConfig.IPAS_ENTITY_MANAGER)
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public QueryBuilder getQueryBuilder(Class<?> entityType) {

        return getFullTextEntityManager().getSearchFactory()
                .buildQueryBuilder()
                .forEntity(entityType)
                .get();
    }

    public FullTextQuery getFullTextQuery(Query luceneQuery, Class<?>... entityTypes) {
        return getFullTextEntityManager().createFullTextQuery(luceneQuery, entityTypes);
    }

    public FullTextSession getFullTextSession() {
        Session session = getEntityManagerFactory().unwrap(SessionFactory.class).openSession();

        return org.hibernate.search.Search.getFullTextSession(session);
    }

    public FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(getEntityManager());
    }
}
