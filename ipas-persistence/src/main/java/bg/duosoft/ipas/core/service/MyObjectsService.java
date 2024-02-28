package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.persistence.model.nonentity.IPObjectHomePanelResult;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.MyObjectsFilter;
import bg.duosoft.ipas.util.filter.MyObjectsHomePanelFilter;

import java.util.List;

public interface MyObjectsService {
    List<IPObjectSimpleResult> getMyObjectsList(MyObjectsFilter filter);
    Integer getMyObjectsCount(MyObjectsFilter filter);

    Integer getNewlyAllocatedObjectsCount(MyObjectsHomePanelFilter filter);


    List<IPObjectHomePanelResult> getMyObjectsForHomePanelList(MyObjectsHomePanelFilter filter);

    Integer getMyObjectsHomePanelCount(MyObjectsHomePanelFilter filter);
}
