package bg.duosoft.ipas.integration.dsclass.service;

import bg.duosoft.ipas.core.model.design.CProductTerm;
import bg.duosoft.ipas.core.model.util.DSClassSearchRequest;
import bg.duosoft.ipas.core.model.util.LocalizationProperties;

import java.util.List;

/**
 * Created by Raya
 * 16.04.2020
 */
public interface DSClassService {

    CProductTerm verifyDesignProductTerm(LocalizationProperties localizationProperties,
                                         String termText, List<String> productClasses);

    List<CProductTerm> searchTerms(DSClassSearchRequest searchRequest);
}
