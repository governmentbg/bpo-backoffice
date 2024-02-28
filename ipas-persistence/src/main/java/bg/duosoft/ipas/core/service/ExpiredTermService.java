package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.util.filter.ExpiredTermFilter;

import java.util.List;
import java.util.Map;

public interface ExpiredTermService {
    List<IPObjectSimpleResult> getExpiredTermsList(ExpiredTermFilter filter);
    Integer getExpiredTersmCount(ExpiredTermFilter filter);
    List<CActionType> getExpiredActionTypes(ExpiredTermFilter filter);
    Map<String, String> getStatuses(ExpiredTermFilter filter);
}
