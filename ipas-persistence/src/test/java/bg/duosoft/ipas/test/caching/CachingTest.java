package bg.duosoft.ipas.test.caching;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.test.TestBase;
import org.infinispan.cache.impl.EncoderCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * User: ggeorgiev
 * Date: 10.6.2019 г.
 * Time: 18:18
 */
public class CachingTest extends TestBase {
    @Autowired
    private FileTypeService fileTypeService;
    @Autowired
    private CacheManager cacheManager;

    @Test
    @Transactional
    public void test() {
        fileTypeService.getFileTypesMap();
        fileTypeService.getFileTypesMap();
        fileTypeService.getFileTypesMap();
        Cache cache = cacheManager.getCache("fileTypesMap");
        EncoderCache nativeCache = (EncoderCache) cache.getNativeCache();
        cache.clear();
        Assert.assertEquals("Size after cleaning should be 0", 0, nativeCache.keySet().size());
        fileTypeService.getFileTypesMap();
        Assert.assertEquals(1, nativeCache.keySet().size());
        Map<String, String> values = (Map<String, String>) nativeCache.get(nativeCache.keySet().iterator().next());
        Assert.assertNotNull(values);
        Assert.assertNotNull(values.get("N"));
    }
}
