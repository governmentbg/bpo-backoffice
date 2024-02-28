package bg.duosoft.ipas.test.i18n;

import bg.duosoft.ipas.core.service.i18n.LanguagePropertiesService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: ggeorgiev
 * Date: 15.12.2021
 * Time: 15:28
 */
public class LangPropertiesTest extends TestBase {
    @Autowired
    private LanguagePropertiesService languagePropertiesService;

    @Ignore
    @Test
    public void testLangProperties() {
        System.out.println(languagePropertiesService.getProperty("bg", "test"));
        System.out.println(languagePropertiesService.getProperty("en", "test"));
        System.out.println(languagePropertiesService.getProperty("bg", "test"));

    }
}
