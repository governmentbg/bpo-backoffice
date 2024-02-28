package bg.duosoft.ipas.test.config;

import bg.duosoft.ipas.core.model.util.LocalizationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.server.i18n.FixedLocaleContextResolver;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * User: Georgi
 * Date: 10.6.2020 г.
 * Time: 12:28
 */
@Configuration
public class TestLocaleConfig {
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages","validation","changeslog");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
