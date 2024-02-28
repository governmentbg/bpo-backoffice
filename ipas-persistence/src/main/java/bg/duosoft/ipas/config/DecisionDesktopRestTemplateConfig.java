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

@Configuration
public class DecisionDesktopRestTemplateConfig {

    @Autowired
    private PropertyAccess propertyAccess;

    @Bean
    public RestTemplate ddProxyRestTemplate() {
        String username = propertyAccess.getDecisionDesktopProxyUser();
        String password = propertyAccess.getDecisionDesktopProxyPass();
        String restServiceURL = propertyAccess.getDecisionDesktopProxyBaseUrl();
        try {
            URI url = new URI(restServiceURL);
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