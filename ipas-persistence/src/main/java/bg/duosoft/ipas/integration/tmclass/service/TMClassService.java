package bg.duosoft.ipas.integration.tmclass.service;

import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.model.util.LocalizationProperties;

/**
 * Created by Raya
 * 14.02.2019
 */
public interface TMClassService {

    CNiceClass verifyClassTerms(LocalizationProperties localizationProperties, String termList, String niceClass);
    CNiceClass getClassHeading(String language, String niceClass);
    CNiceClass getClassAlphaList(String language, String niceClass, String version);
    CNiceClass translateClassTerms(LocalizationProperties localizationProperties, String targetLanguage, String termList, String niceClass);
}
