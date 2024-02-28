package bg.duosoft.ipas.test.caching;

import bg.duosoft.ipas.core.model.structure.OfficeDepartment;
import bg.duosoft.ipas.core.service.structure.OfficeDepartmentService;
import bg.duosoft.ipas.test.StructureTestBase;
import org.infinispan.cache.impl.EncoderCache;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 4.7.2019 г.
 * Time: 18:18
 */
    public class OfficeDepartmentCacheEvictTest extends StructureTestBase {
    @Autowired
    private OfficeDepartmentService officeDepartmentService;
    @Autowired
    private CacheManager cacheManager;

    @Test
    @Transactional
    public void testEvictCache() {
        OfficeDepartment department = officeDepartmentService.getDepartment(MOCK_DIVISION, MOCK_ACTIVE_EMPTY_DEPARTMENT);
        officeDepartmentService.getDepartment(MOCK_DIVISION, MOCK_ACTIVE_EMPTY_DEPARTMENT);

        Cache cache = cacheManager.getCache("departments");
        EncoderCache nativeCache = (EncoderCache) cache.getNativeCache();
        assertEquals("The cache size after getting the department should be 1", 1, nativeCache.keySet().size());
        officeDepartmentService.archiveDepartment(department);
        assertEquals("The cache size after archiving the department should be 0", 0, nativeCache.keySet().size());


    }
}
