package com.duosoft.ipas.config;

import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class UiCacheConfig {
    @Bean
    public SpringEmbeddedCacheManagerFactoryBean uiCacheManager() {
        SpringEmbeddedCacheManagerFactoryBean res = new SpringEmbeddedCacheManagerFactoryBean();
        res.setConfigurationFileLocation(new ClassPathResource("/infinispan-ui.xml"));
        return res;
    }
}
