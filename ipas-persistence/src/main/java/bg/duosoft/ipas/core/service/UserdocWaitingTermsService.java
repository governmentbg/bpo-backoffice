package bg.duosoft.ipas.core.service;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;

import java.util.List;
import java.util.Map;

public interface UserdocWaitingTermsService {
    List<UserdocSimpleResult> getUserdocWaitingTermsList(MyUserdocsFilter filter);
    Integer getUserdocWaitingTermsCount(MyUserdocsFilter filter);
    Map<String,String> getUserdocWaitingTermsUserdocTypesMap(MyUserdocsFilter filter);
}
