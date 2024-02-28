package com.duosoft.ipas.config;

import bg.duosoft.abdocs.model.AbdocsToken;
import bg.duosoft.abdocs.model.response.ApiTokenResponse;
import bg.duosoft.abdocs.service.AbdocsAuthenticator;
import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class AbdocsAdminAuthenticationConfig implements AbdocsAuthenticator {

    @Autowired
    private AbdocsService abdocsServiceAdmin;

    @Autowired
    private YAMLConfig yamlConfig;

    @Autowired
    private AbdocsToken abdocsAdminToken;

    public String getToken() {
        String adminUsername = yamlConfig.getAbdocsSuperUser();
        String token = abdocsAdminToken.getToken();
        if (Objects.isNull(token) || abdocsAdminToken.getExpireDate().compareTo(LocalDateTime.now()) < 0) {
            selectNewToken(adminUsername);
        }
        return abdocsAdminToken.getToken();
    }

    @Override
    public void invalidateToken() {
        abdocsAdminToken.setToken(null);
    }

    private void selectNewToken(String adminUsername) {
        ApiTokenResponse abdocsToken = abdocsServiceAdmin.selectApiToken(adminUsername);
        LocalDateTime expireDateTime = DateUtils.convertToLocalDatTime(abdocsToken.getExpires());
        abdocsAdminToken = new AbdocsToken(abdocsToken.getToken(), expireDateTime);
    }

}
