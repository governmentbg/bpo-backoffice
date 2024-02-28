package bg.duosoft.ipas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "bg.duosoft.ipas.persistence.repository",
        entityManagerFactoryRef = "ipasEntityManager",
        transactionManagerRef = "ipasTransactionManager"
)
@ComponentScan(basePackages = {
        "bg.duosoft.ipas.config",
        "bg.duosoft.ipas.persistence",
        "bg.duosoft.ipas.integration",
        "bg.duosoft.ipas.core",
        "bg.duosoft.ipas.util",
})
@PropertySource("classpath:ipas-persistence/ipas-persistence.properties")
@EnableJpaAuditing
public class IpasDatabaseConfig {

  public static final String IPAS_ENTITY_MANAGER = "ipasEntityManager";

  @Autowired
  private Environment env;

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean ipasEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(ipasDataSource());
    em.setPackagesToScan("bg.duosoft.ipas.persistence");

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);

    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", env.getProperty("ipas.persistence.jpa.database-platform"));
    properties.put("hibernate.show_sql", env.getProperty("ipas.persistence.hibernate.show_sql"));
    properties.put("javax.persistence.validation.mode","none");
    properties.put("hibernate.dynamic-update",true);
   // properties.put("hibernate.format_sql", true);
    em.setJpaPropertyMap(properties);
    return em;
  }

  @Bean
  @Primary
  public DataSource ipasDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("ipas.persistence.datasource.driver-class-name"));
    dataSource.setUrl(env.getProperty("ipas.persistence.datasource.url"));
    dataSource.setUsername(env.getProperty("ipas.persistence.datasource.username"));
    dataSource.setPassword(env.getProperty("ipas.persistence.datasource.password"));

    return dataSource;
  }

  @Bean
  @Primary
  public PlatformTransactionManager ipasTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(ipasEntityManager().getObject());
    return transactionManager;
  }
}