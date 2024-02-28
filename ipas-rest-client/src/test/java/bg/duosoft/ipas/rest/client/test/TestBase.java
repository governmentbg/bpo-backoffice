package bg.duosoft.ipas.rest.client.test;

import bg.duosoft.ipas.rest.client.test.config.TestConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: Georgi
 * Date: 11.9.2020 г.
 * Time: 15:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public abstract class TestBase {

}
