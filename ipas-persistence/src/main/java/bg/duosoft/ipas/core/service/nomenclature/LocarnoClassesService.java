package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.design.CLocarnoClasses;

import java.util.List;


public interface LocarnoClassesService {
    List<CLocarnoClasses> findByLocarnoClassCode(String locarnoClassCode, int maxResults);
    boolean isLocarnoClassExist(String locarnoClassCode);
}
