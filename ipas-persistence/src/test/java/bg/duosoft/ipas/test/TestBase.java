package bg.duosoft.ipas.test;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.config.LoggingMessagesConfigurationConfig;
import bg.duosoft.ipas.test.config.AbdocsMockConfig;
import bg.duosoft.ipas.test.config.LiquibaseConfig;
import bg.duosoft.ipas.test.config.PropertyPlaceholderConfig;
import bg.duosoft.ipas.test.config.TestLocaleConfig;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 1.3.2019 г.
 * Time: 16:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestLocaleConfig.class, IpasDatabaseConfig.class, AbdocsMockConfig.class, LiquibaseConfig.class, PropertyPlaceholderConfig.class, LoggingMessagesConfigurationConfig.class})
@ActiveProfiles({ "unittests" })
public abstract class TestBase {
    protected Gson gson;

    public static <T, U> void _assertEquals(T original, T transformed, Function<T, U> function) {
        assertEquals(function.apply(original), function.apply(transformed));
    }

    @Before
    public void initBase() {
        gson = new Gson();
    }
}
