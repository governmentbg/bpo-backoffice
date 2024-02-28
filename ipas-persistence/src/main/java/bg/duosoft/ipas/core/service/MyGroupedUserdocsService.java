package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;

import java.util.List;
import java.util.Map;

public interface MyGroupedUserdocsService {

    List<UserdocSimpleResult> getMyUserdocsList(MyUserdocsFilter filter);

    Integer getMyUserdocsCount(MyUserdocsFilter filter);

    Integer getNewlyAllocatedUserdocsCount(MyUserdocsFilter filter);

    Map<String, String> getGroupedUserdocStatuses(MyUserdocsFilter filter);

    Map<String, String> getGroupedUserdocObjectType(MyUserdocsFilter filter);

}
