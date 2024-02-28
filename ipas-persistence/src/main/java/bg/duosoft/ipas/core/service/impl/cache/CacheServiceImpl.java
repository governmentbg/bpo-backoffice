package bg.duosoft.ipas.core.service.impl.cache;

import bg.duosoft.ipas.core.service.cache.CacheService;
import bg.duosoft.ipas.persistence.repository.nonentity.JpaCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * User: ggeorgiev
 * Date: 6.6.2019 г.
 * Time: 13:46
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private JpaCacheRepository jpaCacheRepository;

    @Override
    public void clearCache() {
        for(String name : cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear(); // clear cache by name
        }

        jpaCacheRepository.clearCache();//clearing jpa cache
    }
}
