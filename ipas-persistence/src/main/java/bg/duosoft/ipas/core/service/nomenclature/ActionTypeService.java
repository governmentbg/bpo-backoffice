package bg.duosoft.ipas.core.service.nomenclature;


import bg.duosoft.ipas.core.model.action.CActionType;
import java.util.List;

public interface ActionTypeService {

    CActionType findById(String actionTypeCode);

    List<CActionType> findActionTypesByProcTypesAndActionName(List<String> procTypes, String actionName);

    List<CActionType> findActionTypesByFileTypesAndActionName(List<String> fileTypes, String actionName);

    List<CActionType> findAllByAutomaticActionWcode(Integer automaticWCode);

    List<CActionType> findAllByActionTypIn(List<String> actionTyp);
}