package com.duosoft.ipas.config;

import bg.duosoft.abdocs.service.AbdocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AbdocsAuthenticatorRegistrator {

    @Autowired
    private AbdocsService abdocsService;

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private AbdocsAuthenticationConfig abdocsAuthenticationConfig;

    @Autowired
    private AbdocsAdminAuthenticationConfig abdocsAdminAuthenticationConfig;

    @PostConstruct
    public void registerAbdocsAuthenticator() {
        abdocsService.registerAuthenticator(abdocsAuthenticationConfig);
        abdocsServiceAdmin.registerAuthenticator(abdocsAdminAuthenticationConfig);
    }
}
