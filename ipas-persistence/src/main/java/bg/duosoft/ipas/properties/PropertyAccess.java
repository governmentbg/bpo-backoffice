package bg.duosoft.ipas.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertyAccess {

    @Value("${ipas.management.role}")
    private String ipasManagementRole;

    @Value("${portal.rest.service.url}")
    private String portalRestServiceUrl;

    @Value("${portal.rest.service.username}")
    private String portalRestServiceUsername;

    @Value("${portal.rest.service.password}")
    private String portalRestServicePassword;

    @Value("${payment.rest.service.username}")
    private String paymentRestServiceUsername;

    @Value("${payment.rest.service.password}")
    private String paymentRestServicePassword;

    @Value("${payment.rest.service.url}")
    private String paymentRestServiceUrl;

    @Value("${abdocs.rest.service.url}")
    private String abdocsRestServiceUrl;

    @Value("${tmseniority.rest.service.url}")
    private String tmSeniorityRestServiceUrl;

    @Value("${tmclass.verify.rest.service.url}")
    private String tmClassVerityRestServiceUrl;

    @Value("${tmclass.heading.rest.service.url}")
    private String tmClassHeadingRestServiceUrl;

    @Value("${ipas.persistence.hibernate.show_sql}")
    private String ipasPersistenceShowSql;

    @Value("${ipas.persistence.jpa.database-platform}")
    private String ipasPersistenceDatabasePlatform;

    @Value("${ipas.persistence.datasource.driver-class-name}")
    private String ipasPersistenceDatasourceDriverClassName;

    @Value("${ipas.persistence.datasource.username}")
    private String ipasPersistenceDatasourceUsername;

    @Value("${ipas.persistence.datasource.password}")
    private String ipasPersistenceDatasourcePassword;

    @Value("${liferay.remote.user.name}")
    private String liferayRemoteUserName;

    @Value("${offidoc.static.templates.directory}")
    private String offidocStaticTemplatesDirectory;


    @Value("${decisiondesktop.proxy.baseUrl}")
    private String decisionDesktopProxyBaseUrl;

    @Value("${decisiondesktop.proxy.user}")
    private String decisionDesktopProxyUser;

    @Value("${decisiondesktop.proxy.pass}")
    private String decisionDesktopProxyPass;

    @Value("${decisiondesktop.draftingeditor.app.url}")
    private String draftingEditorAppUrl;

    @Value("${efuserchange.url}")
    private String efuserchangeUrl;
    @Value("${efuserchange.username}")
    private String efuserchangeUsername;
    @Value("${efuserchange.password}")
    private String efuserchangePassword;
}
