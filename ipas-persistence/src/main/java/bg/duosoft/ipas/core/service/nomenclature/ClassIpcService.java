package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.miscellaneous.CCpcClass;
import bg.duosoft.ipas.core.model.miscellaneous.CIpcClass;
import bg.duosoft.ipas.core.model.patent.CPatentCpcClass;
import bg.duosoft.ipas.core.model.patent.CPatentIpcClass;

import java.util.List;

public interface ClassIpcService {

    CIpcClass findById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode);

    List<CIpcClass> findBySectionClassSubclassGroupAndSubgroup(String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode);
    List<CIpcClass> findIpcClassesByIpcNumber(String ipcNumber, int maxResults);
    List<CIpcClass> findIpcClassesByIpcNumberForSpcs(String ipcNumber, int maxResults,String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);
    CIpcClass save(CIpcClass ipcClass);
    List<CIpcClass> getValidIpcsById(String editionCode, String sectionCode, String classCode, String subclassCode, String groupCode, String subgroupCode);

    List<CIpcClass> findAllIpcClassesByIpcNumber(String ipcNumber, int maxResults);

    CIpcClass findIpcClassByIpcNumber(String ipcNumber);

    List<CPatentIpcClass> copyFromCpcList(List<CPatentIpcClass>ipcList, List<CPatentCpcClass>cpcList);
}
