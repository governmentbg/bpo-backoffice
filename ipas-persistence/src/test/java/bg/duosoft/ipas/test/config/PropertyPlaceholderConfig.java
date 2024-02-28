package bg.duosoft.ipas.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * User: ggeorgiev
 * Date: 16.03.2021
 * Time: 14:34
 */
@Configuration
@PropertySource("classpath:hibernate.properties")
public class PropertyPlaceholderConfig {
}
