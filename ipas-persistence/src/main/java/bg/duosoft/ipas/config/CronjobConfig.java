package bg.duosoft.ipas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * User: ggeorgiev
 * Date: 06.04.2021
 * Time: 17:17
 */
@Configuration
@ImportResource({"classpath*:/cronjob-config.xml"})
@PropertySource("classpath:cronjob-persistence.properties")
@ComponentScan(basePackages = {
        "bg.duosoft.ipas.cronjob",
})
@Profile("!unittests")
public class CronjobConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource cronjobDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("cronjob.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("cronjob.datasource.jdbcUrl"));
        dataSource.setUsername(env.getProperty("cronjob.datasource.username"));
        dataSource.setPassword(env.getProperty("cronjob.datasource.password"));
        return dataSource;
    }
}
