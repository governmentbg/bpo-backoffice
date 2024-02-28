package com.duosoft.ipas.config;

import bg.duosoft.abdocs.model.AbdocsToken;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.abdocs.service.impl.AbdocsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AbdocsAdminConfig {

    @Bean
    public AbdocsService abdocsServiceAdmin(){
        return new AbdocsServiceImpl();
    }

    @Bean
    public AbdocsToken abdocsAdminToken(){
        return new AbdocsToken();
    }

}
