package bg.duosoft.ipas.rest.client.test.config;

import bg.duosoft.ipas.rest.config.IpasRestClientInterceptorsConfiguration;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Georgi
 * Date: 11.9.2020 г.
 * Time: 15:55
 */
@Configuration
@ComponentScan(basePackages = {"bg.duosoft.ipas.rest"})
public class TestConfig {
    @Value("${ipas.rest.service.default.execution.username}")
    private String defaultExecutionUsername;
    @Bean
    public static PropertyPlaceholderConfigurer propConfig() {
        PropertyPlaceholderConfigurer ppc =  new PropertyPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("rest-client-test.properties"));
        return ppc;
    }

    @Bean
    public IpasRestClientInterceptorsConfiguration ipasRestClientInterceptorsConfiguration() {
        return new IpasRestClientInterceptorsConfiguration() {
            @Override
            public List<Interceptor<Message>> getInInterceptors() {
                List<Interceptor<Message>> res = new ArrayList<>();
                res.add(new LoggingInInterceptor(-1));
                return res;
            }

            @Override
            public List<Interceptor<Message>> getOutInterceptors() {
                List<Interceptor<Message>> res = new ArrayList<>();
                res.add(new LoggingOutInterceptor(-1));
                res.add(new DefaultRequestUserInterceptor(defaultExecutionUsername));
                return res;
            }
        };
    }

}
