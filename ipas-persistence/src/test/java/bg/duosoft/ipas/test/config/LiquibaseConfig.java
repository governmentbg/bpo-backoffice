package bg.duosoft.ipas.test.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: Georgi
 * Date: 11.7.2020 г.
 * Time: 23:56
 */
@Configuration
public class LiquibaseConfig {
    @Autowired
    private DataSource dataSource;

    @Value("${ipas.liquibase.init}")
    private boolean initLiquibase;


    @PostConstruct
    public void iniLiquibase() throws SQLException, LiquibaseException {
        if (initLiquibase) {
            try (Connection c = dataSource.getConnection()) {
                c.setAutoCommit(false);
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
                database.setLiquibaseSchemaName("LIQUIBASE");
                Liquibase liquibase = new liquibase.Liquibase("db/db.master.xml", new ClassLoaderResourceAccessor(), database);
                liquibase.update(new Contexts(), new LabelExpression());
                c.commit();
            }
        }

    }
}


