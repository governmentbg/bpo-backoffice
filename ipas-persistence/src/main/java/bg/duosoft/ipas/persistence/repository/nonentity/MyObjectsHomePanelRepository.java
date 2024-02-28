package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.IPObjectHomePanelResult;
import bg.duosoft.ipas.util.filter.MyObjectsHomePanelFilter;

import java.util.List;

public interface MyObjectsHomePanelRepository {
     List<IPObjectHomePanelResult> getMyObjectsList(MyObjectsHomePanelFilter filter);
     Integer getMyObjectsCount(MyObjectsHomePanelFilter filter);
     Integer getNewlyAllocatedObjectsCount(MyObjectsHomePanelFilter filter);
}
