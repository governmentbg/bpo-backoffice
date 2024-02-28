package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.MyObjectsFilter;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;

import java.util.List;
import java.util.Map;

public interface MyGroupedUserdocsRepository {

     List<UserdocSimpleResult> getMyUserdocsList(MyUserdocsFilter filter);

     Integer getMyUserdocsCount(MyUserdocsFilter filter);

     Integer getNewlyAllocatedUserdocsCount(MyUserdocsFilter filter);

     Map<String, String> getGroupedUserdocStatuses(MyUserdocsFilter filter);

     Map<String, String> getGroupedUserdocObjectType(MyUserdocsFilter filter);

}
