package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.MyObjectsFilter;

import java.util.List;

public interface MyObjectsRepository {
     List<IPObjectSimpleResult> getMyObjectsList(MyObjectsFilter filter);
     Integer getMyObjectsCount(MyObjectsFilter filter);
}
