package bg.duosoft.ipas.config;

import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

/**
 * User: ggeorgiev
 * Date: 6.6.2019 г.
 * Time: 13:36
 */
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    @Primary
    public SpringEmbeddedCacheManagerFactoryBean cacheManager() {
        SpringEmbeddedCacheManagerFactoryBean res = new SpringEmbeddedCacheManagerFactoryBean();
        res.setConfigurationFileLocation(new ClassPathResource("/infinispan.xml"));
        return res;
    }

}
