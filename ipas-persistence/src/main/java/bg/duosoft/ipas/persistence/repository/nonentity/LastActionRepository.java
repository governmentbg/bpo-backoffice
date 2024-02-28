package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.LastActionsResult;
import bg.duosoft.ipas.util.filter.LastActionFilter;

import java.util.List;
import java.util.Map;

public interface LastActionRepository {

    List<LastActionsResult> getLastActionList(LastActionFilter filter);

    int getLastActionCount(LastActionFilter filter);

    Map<String,String> getLastActionFileTypes(LastActionFilter filter);

    Map<String,String> getLastActionUserdocTypes(LastActionFilter filter);
}
