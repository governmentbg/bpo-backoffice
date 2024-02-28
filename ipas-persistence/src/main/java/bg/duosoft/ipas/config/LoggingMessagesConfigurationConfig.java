package bg.duosoft.ipas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User: ggeorgiev
 * Date: 03.12.2021
 * Time: 15:09
 */
@Configuration
public class LoggingMessagesConfigurationConfig {

    @Bean
    public Map<String, String> loggingMessagesConfiguration() throws IOException {
        InputStream is = LoggingMessagesConfigurationConfig.class.getClassLoader().getResourceAsStream("logging-mapper-config.properties");
        Properties props = new Properties();
        props.load(is);
        Map<String, String> res = new HashMap<>();
        for (String prop : props.stringPropertyNames()) {
            res.put(prop, props.getProperty(prop));
        }
        return res;
    }
}
