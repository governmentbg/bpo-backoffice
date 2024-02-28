package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.miscellaneous.CClassCpcIpcConcordance;

public interface ClassCpcIpcConcordanceService {
    CClassCpcIpcConcordance findById(String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode);
}
