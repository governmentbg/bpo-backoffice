package bg.duosoft.ipas.config;

import bg.bpo.ebd.ebddpersistence.config.EbdDatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EbdServicesConfig extends EbdDatabaseConfig{
    @Bean(name = "ebdHikariConfig")
    public HikariConfig ebdHikariConfig() {
        HikariConfig config = super.ebdHikariConfig();
        config.setInitializationFailTimeout(-1);
        return config;
    }

}