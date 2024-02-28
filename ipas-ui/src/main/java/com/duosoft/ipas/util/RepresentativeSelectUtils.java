package com.duosoft.ipas.util;

import bg.duosoft.ipas.enums.RepresentativeType;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.LinkedHashMap;
import java.util.Map;

public class RepresentativeSelectUtils {

    public static Map<RepresentativeType, String> generateRepresentativeTypeSelectOptions(MessageSource messageSource) {
        Map<RepresentativeType, String> representativeTypeMap = new LinkedHashMap<>();
        representativeTypeMap.put(RepresentativeType.NATURAL_PERSON, messageSource.getMessage("representativeType.select.NATURAL_PERSON", null, LocaleContextHolder.getLocale()));
        representativeTypeMap.put(RepresentativeType.PARTNERSHIP, messageSource.getMessage("representativeType.select.PARTNERSHIP", null, LocaleContextHolder.getLocale()));
        representativeTypeMap.put(RepresentativeType.LAWYER, messageSource.getMessage("representativeType.select.LAWYER", null, LocaleContextHolder.getLocale()));
        representativeTypeMap.put(RepresentativeType.LAWYER_COMPANY, messageSource.getMessage("representativeType.select.LAWYER_COMPANY", null, LocaleContextHolder.getLocale()));
        representativeTypeMap.put(RepresentativeType.LAWYER_PARTNERSHIP, messageSource.getMessage("representativeType.select.LAWYER_PARTNERSHIP", null, LocaleContextHolder.getLocale()));
        representativeTypeMap.put(RepresentativeType.TEMP_SERVICE_PERSON, messageSource.getMessage("representativeType.select.TEMP_SERVICE_PERSON", null, LocaleContextHolder.getLocale()));
        return representativeTypeMap;
    }

}
