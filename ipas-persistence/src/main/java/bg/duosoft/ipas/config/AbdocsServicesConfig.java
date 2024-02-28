package bg.duosoft.ipas.config;

import bg.duosoft.abdocs.config.AbdocsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AbdocsConfig.class)
public class AbdocsServicesConfig {

}