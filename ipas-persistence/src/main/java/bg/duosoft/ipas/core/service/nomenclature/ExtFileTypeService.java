package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.structure.CExtFileType;

import java.util.List;
import java.util.Map;

public interface ExtFileTypeService {
    Map<String, String> getFileTypesAsMapAndOrdered();
}
