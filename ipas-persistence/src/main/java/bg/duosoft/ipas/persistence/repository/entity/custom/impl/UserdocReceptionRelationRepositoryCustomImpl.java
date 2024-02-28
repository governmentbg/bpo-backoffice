package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocMainTypeSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.UserdocReceptionRelationRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserdocReceptionRelationRepositoryCustomImpl extends BaseRepositoryCustomImpl implements UserdocReceptionRelationRepositoryCustom {
    @Autowired
    private StringToBooleanMapper stringToBooleanMapper;

    @Override
    public Map<String, String> selectMainTypesMap() {
        String buildQuery = buildMainTypesQuery();
        Query query = em.createNativeQuery(buildQuery);
        return fillMapResult(query);
    }

    @Override
    public List<UserdocMainTypeSimpleResult> selectMainTypesByUserdocType(String userdocType) {
        String buildQuery = buildMainTypesByUserdocTypeQuery();
        Query query = em.createNativeQuery(buildQuery);
        query.setParameter("userdocType", userdocType);
        return fillObjectResult(query);
    }

    private String buildMainTypesQuery() {
        StringBuilder builder = new StringBuilder()
                .append("SELECT ft.FILE_TYP, ft.FILE_TYPE_NAME ")
                .append("FROM IPASPROD.CF_FILE_TYPE ft ")
                .append("UNION ")
                .append("SELECT ut.USERDOC_TYP, ut.USERDOC_NAME ")
                .append("FROM IPASPROD.CF_USERDOC_TYPE ut ");

        return builder.toString();
    }

    private String buildMainTypesByUserdocTypeQuery() {
        StringBuilder builder = new StringBuilder()
                .append("SELECT ft.FILE_TYP, ft.FILE_TYPE_NAME, ur.IS_VISIBLE ")
                .append("FROM IPASPROD.CF_FILE_TYPE ft ")
                .append("JOIN EXT_RECEPTION.USERDOC_RECEPTION_RELATION ur ON ur.MAIN_TYPE = ft.FILE_TYP ")
                .append("WHERE ur.LINKED_USERDOC_TYPE = :userdocType ")
                .append("UNION ")
                .append("SELECT ut.USERDOC_TYP, ut.USERDOC_NAME, ur.IS_VISIBLE ")
                .append("FROM IPASPROD.CF_USERDOC_TYPE ut ")
                .append("JOIN EXT_RECEPTION.USERDOC_RECEPTION_RELATION ur ON ur.MAIN_TYPE = ut.USERDOC_TYP ")
                .append("WHERE ur.LINKED_USERDOC_TYPE = :userdocType ");

        return builder.toString();
    }

    private Map<String, String> fillMapResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        Map<String, String> mainTypesMap =  resultList.stream().collect(Collectors.toMap(obj -> (String) obj[0], obj -> (String) obj[1]));
        return mainTypesMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private List<UserdocMainTypeSimpleResult> fillObjectResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        return resultList.stream()
                .map(object -> new UserdocMainTypeSimpleResult(
                        (String) object[0], (String) object[1], stringToBooleanMapper.textToBoolean((String)object[2])
                ))
                .collect(Collectors.toList());
    }

}
