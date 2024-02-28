package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.LastActionsResult;
import bg.duosoft.ipas.util.filter.LastActionFilter;

import java.util.List;
import java.util.Map;

public interface LastActionService {

    List<LastActionsResult> getLastActionList(LastActionFilter filter);

    int getLastActionCount(LastActionFilter filter);

    Map<String,String> getLastActionFileTypes(LastActionFilter filter);

    Map<String, String> getLastActionUserdocTypes(LastActionFilter filter);

}
