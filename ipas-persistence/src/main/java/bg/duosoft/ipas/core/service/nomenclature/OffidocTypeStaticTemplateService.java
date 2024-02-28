package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.offidoc.COffidocTypeStaticTemplate;

import java.util.List;

public interface OffidocTypeStaticTemplateService {

    List<COffidocTypeStaticTemplate> selectAllTemplates(String offidocType);

}
