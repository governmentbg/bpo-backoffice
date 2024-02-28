package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.WaitingTermFilter;

import java.util.List;
import java.util.Map;

public interface WaitingTermService {

    List<IPObjectSimpleResult> getWaitingTermList(WaitingTermFilter filter);

    Integer getWaitingTermCount(WaitingTermFilter filter);

    Map<String, String> getStatuses(WaitingTermFilter filter);

}
