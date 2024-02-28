package bg.duosoft.ipas.core.service.nomenclature;

import java.util.List;
import java.util.Map;

public interface FileTypeService {

    Map<String,String> getFileTypesMap();

    Map<String,String> getFileTypesMapBasedOnSecurityRights();

    Map<String,String> findAllByFileTypInOrderByFileTypeName(List<String> fileTypes);
}