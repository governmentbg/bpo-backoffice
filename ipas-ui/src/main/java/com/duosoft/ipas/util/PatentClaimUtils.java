package com.duosoft.ipas.util;

import bg.duosoft.ipas.core.model.patent.CClaim;
import bg.duosoft.ipas.enums.PatentRelationshipExtApplType;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.LinkedHashMap;
import java.util.List;

public class PatentClaimUtils {

    public static Long getClaimsMaxNumber(List<CClaim> cClaims) {
        long maxClaim = cClaims.stream().mapToLong(r -> r.getClaimNbr()).max().orElse(0);
        return ++maxClaim;
    }

    public static LinkedHashMap<String, String> generatePatentClaimsSelectOptions(MessageSource messageSource, boolean hasNationalPatentSupport,String relationshipType) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(PatentRelationshipExtApplType.EUROPEAN_PATENT.code(), messageSource.getMessage("patent.transformation.european.patent."+relationshipType, null, LocaleContextHolder.getLocale()));
        map.put(PatentRelationshipExtApplType.INTERNATIONAL_PATENT.code(), messageSource.getMessage("patent.transformation.wipo.patent."+relationshipType, null, LocaleContextHolder.getLocale()));
        if (hasNationalPatentSupport){
            map.put(PatentRelationshipExtApplType.NATIONAL_PATENT.code(), messageSource.getMessage("patent.transformation.national.patent."+relationshipType, null, LocaleContextHolder.getLocale()));
        }
        return map;
    }
}
