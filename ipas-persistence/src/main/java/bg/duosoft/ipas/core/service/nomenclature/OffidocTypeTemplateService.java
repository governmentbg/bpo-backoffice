package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.offidoc.COffidocTypeTemplate;

public interface OffidocTypeTemplateService {
     String findNameFileConfigById(String offidocTyp,String nameWfile);
     COffidocTypeTemplate selectById(String offidocTyp, String nameWFile);
     void updateOffidocTemplateConfig(String offidocTyp, String nameWFile, String nameConfig);
}
