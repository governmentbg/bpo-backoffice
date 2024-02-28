package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLaw;

import java.util.Map;

public interface LawService {

    Map<Integer,String> getLawMap();

    CfLaw findById(Integer id);

    Integer getLawIdByApplicationTypeAndSubtype(String applicationType, String applicationSubType);

}