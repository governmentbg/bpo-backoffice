package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.CApplicationSubType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public interface ApplicationTypeService {

    Map<String,String> getApplicationTypesMap(String tableName);

    Map<String,String> getApplicationTypesMap(String tableName,List<String> fileTypes);

    List<CApplicationSubType> findAllApplicationSubTypesByApplTyp(String applTyp);

    List<CApplicationSubType> findAllSingleDesignApplicationSubTypesByMasterApplTyp(String applTyp);

    CApplicationSubType getApplicationSubtype(String applTyp, String applSubTyp);

    String selectTableNameByFileType(String fileTyp);

    String selectApplicationTypeByFileType(String fileTyp);

    String getFileTypeByApplicationType(String applicationType);

    String getDefaultApplicationSubtype(String applicationType);

    Map<String, String> getApplicationTypesMapOrderByApplTypeName(List<String> fileTypes);

    List<CApplicationSubType> getSubTypesByFileTypesOrderByApplTypeName(List<String> fileTypes);

    List<CApplicationSubType> getSubTypesByFileTypesApplTypOrderByApplTypeName(List<String> fileTypes, String applTyp);
}