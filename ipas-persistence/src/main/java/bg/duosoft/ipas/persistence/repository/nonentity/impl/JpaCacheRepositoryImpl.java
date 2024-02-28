package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.JpaCacheRepository;
import org.springframework.stereotype.Repository;

/**
 * User: ggeorgiev
 * Date: 18.04.2023
 * Time: 12:03
 */
@Repository
public class JpaCacheRepositoryImpl extends BaseRepositoryCustomImpl implements JpaCacheRepository {
    @Override
    public void clearCache() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
}
