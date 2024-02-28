package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.miscellaneous.CGeoCountry;

import java.util.Map;

public interface CountryService {

    CGeoCountry findById(String code);

    Map<String,String> getCountryMap();

}