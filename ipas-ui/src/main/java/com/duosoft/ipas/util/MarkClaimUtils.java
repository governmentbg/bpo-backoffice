package com.duosoft.ipas.util;

import bg.duosoft.ipas.enums.MarkTransformationType;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.LinkedHashMap;

public class MarkClaimUtils {

    public static LinkedHashMap<String, String> generateMarkClaimsSelectOptions(MessageSource messageSource) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(MarkTransformationType.EUROPEAN_MARK.code(), messageSource.getMessage("mark.transformation.type.EM", null, LocaleContextHolder.getLocale()));
        map.put(MarkTransformationType.INTERNATIONAL_MARK.code(), messageSource.getMessage("mark.transformation.type.WO", null, LocaleContextHolder.getLocale()));
        return map;
    }

}
