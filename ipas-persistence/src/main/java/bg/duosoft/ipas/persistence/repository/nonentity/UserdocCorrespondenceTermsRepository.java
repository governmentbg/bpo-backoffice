package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;

import java.util.List;
import java.util.Map;

public interface UserdocCorrespondenceTermsRepository {
    List<UserdocSimpleResult> getUserdocCorrespondenceTermsList(MyUserdocsFilter filter);
    Integer getUserdocCorrespondenceTermsCount(MyUserdocsFilter filter);
    Map<String, String> getUserdocCorrespondenceTermsUserdocTypesMap(MyUserdocsFilter filter);
    Map<String, String> getStatuses(MyUserdocsFilter filter);
    Map<String, String> getUserdocCorrespondenceTermsObjectType(MyUserdocsFilter filter);
}
