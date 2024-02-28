package com.duosoft.ipas.config;


import bg.duosoft.ipas.core.model.util.TempID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class TemporaryObjectsConfig {
    public static final Integer TEMP_ID_INITIAL_VALUE = -1;

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TempID tempId() {
        TempID tempID = new TempID();
        tempID.setValue(TEMP_ID_INITIAL_VALUE);
        return tempID;
    }

}
