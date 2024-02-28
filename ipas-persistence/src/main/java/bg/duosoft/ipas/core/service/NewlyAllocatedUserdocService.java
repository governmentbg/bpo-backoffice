package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.persistence.model.nonentity.NewlyAllocatedUserdocSimpleResult;
import bg.duosoft.ipas.util.filter.NewlyAllocatedUserdocFilter;

import java.util.List;
import java.util.Map;

public interface NewlyAllocatedUserdocService {
    List<NewlyAllocatedUserdocSimpleResult> selectNewlyAllocatedUserdocs(NewlyAllocatedUserdocFilter filter);

    int selectNewlyAllocatedUserdocsCount(NewlyAllocatedUserdocFilter filter);

    Map<String, String> getUserdocTypes(NewlyAllocatedUserdocFilter filter);

    Map<String, String> getUserdocObjectTypes(NewlyAllocatedUserdocFilter filter);
}
