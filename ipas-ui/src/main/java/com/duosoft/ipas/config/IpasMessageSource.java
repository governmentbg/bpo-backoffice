package com.duosoft.ipas.config;

import bg.duosoft.ipas.core.service.i18n.LanguagePropertiesService;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * User: ggeorgiev
 * Date: 15.12.2021
 * Time: 14:45
 */
public class IpasMessageSource extends ResourceBundleMessageSource {
    private LanguagePropertiesService languagePropertiesService;

    public IpasMessageSource(LanguagePropertiesService languagePropertiesService) {
        this.languagePropertiesService = languagePropertiesService;
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String message = languagePropertiesService.getProperty(locale.getLanguage(), code);
        if (message != null) {
            return message;
        }
        return super.resolveCodeWithoutArguments(code, locale);
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String message = languagePropertiesService.getProperty(locale.getLanguage(), code);
        if (message != null) {
            return new MessageFormat(message, locale);
        }
        return super.resolveCode(code, locale);
    }
}
