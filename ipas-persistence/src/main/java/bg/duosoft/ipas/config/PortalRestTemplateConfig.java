package bg.duosoft.ipas.config;

import bg.duosoft.ipas.properties.PropertyAccess;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
public class PortalRestTemplateConfig {

    @Autowired
    private PropertyAccess propertyAccess;

    @Bean
    public RestTemplate portalRestTemplate() {
        String username = propertyAccess.getPortalRestServiceUsername();
        String password = propertyAccess.getPortalRestServicePassword();
        String restServiceUrl = propertyAccess.getPortalRestServiceUrl();
        try {
            URI url = new URI(Objects.requireNonNull(restServiceUrl));
            HttpHost host = new HttpHost(url.getHost());
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactoryBasicAuth(host));
            List<ClientHttpRequestInterceptor> interceptors = Collections.singletonList(new BasicAuthInterceptor(username, password));
            restTemplate.setRequestFactory(new InterceptingClientHttpRequestFactory(restTemplate.getRequestFactory(), interceptors));
            return restTemplate;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}