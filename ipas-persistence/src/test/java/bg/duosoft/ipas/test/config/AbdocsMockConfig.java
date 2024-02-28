package bg.duosoft.ipas.test.config;

import bg.duosoft.abdocs.service.AbdocsService;
import bg.duosoft.ipas.test.mock.AbdocsMockServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * User: Georgi
 * Date: 18.6.2020 г.
 * Time: 13:52
 */
@Configuration
public class AbdocsMockConfig {
    @Bean
    @Primary
    public AbdocsService abdocsService() {
        return new AbdocsMockServiceImpl();
    }
}
