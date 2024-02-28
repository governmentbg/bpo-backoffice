package bg.duosoft.ipas.core.service.nomenclature;

import bg.duosoft.ipas.core.model.reception.CProcessType;

import java.util.List;
import java.util.Map;

public interface ProcessTypeService {

    Map<String,String> getProcessTypeMap();

    boolean isProcessTypeForManualSubProcess(String processType);

    List<CProcessType> selectByRelatedToWcode(Integer relatedToWcode);

}