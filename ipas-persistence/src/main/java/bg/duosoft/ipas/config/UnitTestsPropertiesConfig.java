package bg.duosoft.ipas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class UnitTestsPropertiesConfig {

    //In order to populate fields with @Value annotation in your test you need to configure PropertySourcesPlaceholderConfigurer.
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}