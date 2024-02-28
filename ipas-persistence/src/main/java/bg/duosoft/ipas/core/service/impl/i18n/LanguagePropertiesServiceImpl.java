package bg.duosoft.ipas.core.service.impl.i18n;

import bg.duosoft.ipas.core.service.i18n.LanguagePropertiesService;
import bg.duosoft.ipas.persistence.model.entity.i18n.LangProperties;
import bg.duosoft.ipas.persistence.model.entity.i18n.LangPropertiesPK;
import bg.duosoft.ipas.persistence.repository.entity.i18n.LangPropertiesRepository;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.cache.impl.EncoderCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * User: ggeorgiev
 * Date: 15.12.2021
 * Time: 15:23
 */
@Service
@Slf4j
public class LanguagePropertiesServiceImpl implements LanguagePropertiesService {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private LangPropertiesRepository langPropertiesRepository;

    @Override
    @Cacheable(value = "langProperties", key = "new bg.duosoft.ipas.persistence.model.entity.i18n.LangPropertiesPK(#lang, #key)")
    public String getProperty(String lang, String key) {
        Cache cache = cacheManager.getCache("langProperties");
        EncoderCache nativeCache = (EncoderCache) cache.getNativeCache();
        if (nativeCache.size() == 0) {
            synchronized (this) {
                log.debug("Cache is empty...Caching all elements...");
                List<LangProperties> all = langPropertiesRepository.findAll();
                if (!CollectionUtils.isEmpty(all)) {
                    all.stream().forEach(e -> cache.put(e.getPk(), e.getValue()));
                } else {
                    cache.put(new LangPropertiesPK(null, null), null);//setting empty record to the cache, so the nativeCache's size to be greater than 0 for the next call
                }
            }
        }
        LangPropertiesPK k = new LangPropertiesPK(lang, key);
        return cache.get(k) == null ? null : (String) cache.get(k).get();
    }
}
