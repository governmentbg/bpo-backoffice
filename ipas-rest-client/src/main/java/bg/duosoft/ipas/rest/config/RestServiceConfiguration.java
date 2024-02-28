package bg.duosoft.ipas.rest.config;

import bg.duosoft.ipas.rest.client.proxy.*;
import bg.duosoft.ipas.rest.interceptors.CheckResponseForErrorsInterceptor;
import bg.duosoft.ipas.rest.interceptors.IpasAuthenticationInterceptor;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.*;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 18:44
 */
@Configuration
public class RestServiceConfiguration {
    @Autowired(required = false)
    private IpasRestClientInterceptorsConfiguration ipasRestClientInterceptorsConfiguration;
    @Value("${ipas.rest.service.url}")
    private String ipasRestUrl;
    @Value("${ipas.rest.service.username:#{null}}")
    private String ipasRestUsername;
    @Value("${ipas.rest.service.password:#{null}}")
    private String ipasRestPassword;

    @Bean
    public AbdocsRestProxy abdocsRestProxy() {
        return createProxy(AbdocsRestProxy.class);
    }
    @Bean
    public DesignRestProxy designRestProxy() {
        return createProxy(DesignRestProxy.class);
    }
    @Bean
    public EpoPatentRestProxy epoPatentRestProxy() {
        return createProxy(EpoPatentRestProxy.class);
    }
    @Bean
    public PatentRestProxy patentRestProxy() {
        return createProxy(PatentRestProxy.class);
    }
    @Bean
    public TrademarkRestProxy trademarkRestProxy() {
        return createProxy(TrademarkRestProxy.class);
    }
    @Bean
    public UserdocRestProxy userdocRestProxy() {
        return createProxy(UserdocRestProxy.class);
    }
    @Bean
    public CommonServicesRestProxy workingDateRestProxy() {
        return createProxy(CommonServicesRestProxy.class);
    }
    @Bean
    public ProcessRestProxy processRestProxy() {
        return createProxy(ProcessRestProxy.class);
    }
    @Bean
    public PersonRestProxy personRestProxy() {
        return createProxy(PersonRestProxy.class);
    }
    @Bean
    public SqlRestProxy internalSqlRestProxy() {
        return createProxy(SqlRestProxy.class);
    }
    @Bean
    public EfilingDataRestProxy efilingDataRestProxy(){
        return createProxy(EfilingDataRestProxy.class);
    }
    @Bean
    public ReceptionRestProxy receptionRestProxy() {
        return createProxy(ReceptionRestProxy.class);
    }
    @Bean
    public SearchRestProxy searchRestProxy() {
        return createProxy(SearchRestProxy.class);
    }
    @Bean
    public OffidocRestProxy offidocRestProxy() {
        return createProxy(OffidocRestProxy.class);
    }
    private <T> T createProxy(Class<T> cls) {
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJsonProvider());
        providers.add(new IpasRestServiceExceptionMapper());


        T res = JAXRSClientFactory.create(ipasRestUrl, cls, providers);
        ClientConfiguration config = WebClient.getConfig(res);

        if (ipasRestClientInterceptorsConfiguration == null ||
                (ipasRestClientInterceptorsConfiguration.getDefaultInterceptors() != null && ipasRestClientInterceptorsConfiguration.getDefaultInterceptors().contains(IpasAuthenticationInterceptor.class))) {
            config.getOutInterceptors().add(new IpasAuthenticationInterceptor(ipasRestUsername, ipasRestPassword));
        }
        if (ipasRestClientInterceptorsConfiguration == null ||
                (ipasRestClientInterceptorsConfiguration.getDefaultInterceptors() != null && ipasRestClientInterceptorsConfiguration.getDefaultInterceptors().contains(CheckResponseForErrorsInterceptor.class))) {
            config.getInInterceptors().add(new CheckResponseForErrorsInterceptor());
        }

        if (ipasRestClientInterceptorsConfiguration != null) {
            if (ipasRestClientInterceptorsConfiguration.getInInterceptors() != null) {
                config.getInInterceptors().addAll(ipasRestClientInterceptorsConfiguration.getInInterceptors());
            }
            if (ipasRestClientInterceptorsConfiguration.getInFaultInterceptors() != null) {
                config.getInFaultInterceptors().addAll(ipasRestClientInterceptorsConfiguration.getInFaultInterceptors());
            }
            if (ipasRestClientInterceptorsConfiguration.getOutInterceptors() != null) {
                config.getOutInterceptors().addAll(ipasRestClientInterceptorsConfiguration.getOutInterceptors());
            }
            if (ipasRestClientInterceptorsConfiguration.getOutFaultInterceptors() != null) {
                config.getOutFaultInterceptors().addAll(ipasRestClientInterceptorsConfiguration.getOutFaultInterceptors());
            }
        }

        config.getHttpConduit().getClient().setAllowChunking(false);


        HTTPConduit conduit = config.getHttpConduit();
        conduit.getClient().setConnectionTimeout(1000 * 30); //30 seconds
        conduit.getClient().setReceiveTimeout(1000 * 600); //10 minutes

        return res;
    }

}
