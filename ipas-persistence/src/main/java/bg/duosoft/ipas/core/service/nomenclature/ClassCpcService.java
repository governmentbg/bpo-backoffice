package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;

import java.util.List;

public interface ClassCpcService {

    CCpcClass findById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode);

    List<CCpcClass> findBySectionClassSubclassGroupAndSubgroup(String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode);
    List<CCpcClass> findCpcClassesByCpcNumber(String cpcNumber, int maxResults);
    List<CCpcClass> findCpcClassesByCpcNumberForSpcs(String cpcNumber, int maxResults,String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);
    CCpcClass save(CCpcClass cpcClass);
    List<CCpcClass> getValidCpcsById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode);

    List<CCpcClass> findAllCpcClassesByCpcNumber(String cpcNumber, int maxResults);

    CCpcClass findCpcClassByCpcNumber(String cpcNumber);
}
