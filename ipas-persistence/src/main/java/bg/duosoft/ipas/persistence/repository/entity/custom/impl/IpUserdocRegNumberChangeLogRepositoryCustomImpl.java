package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.nonentity.SimpleUserdocRegNumberChangeLog;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpUserdocRegNumberChangeLogRepositoryCustom;
import bg.duosoft.ipas.util.filter.UserdocRegNumberChangeLogFilter;
import bg.duosoft.ipas.util.filter.sorter.UserdocRegNumberChangeLogSorterUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class IpUserdocRegNumberChangeLogRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpUserdocRegNumberChangeLogRepositoryCustom {
    @Override
    public List<SimpleUserdocRegNumberChangeLog> getSimpleUserdocRegNumberChangeLogList(UserdocRegNumberChangeLogFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillResult(query);
    }

    @Override
    public Integer getSimpleUserdocRegNumberChangeLogCount(UserdocRegNumberChangeLogFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(UserdocRegNumberChangeLogFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT")
                .append(isCount ? " COUNT(*)" : " l.DOC_ORI, l.DOC_LOG, l.DOC_SER, l.DOC_NBR, l.OLD_REGISTRATION_NUMBER, l.NEW_REGISTRATION_NUMBER, l.DATE, u.USER_NAME")
                .append(" FROM EXT_CORE.IP_USERDOC_REGISTRATION_NUMBER_CHANGE_LOG l ")
                .append(" LEFT JOIN IPASPROD.IP_USER u ON u.LOGIN = l.USERNAME")
                .append(" WHERE 1=1 ");

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND u.USER_ID = :responsibleUser ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = UserdocRegNumberChangeLogSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        return queryBuilder.toString();
    }

    private void addQueryParams(UserdocRegNumberChangeLogFilter filter, Query query) {
        Integer responsibleUser = filter.getResponsibleUser();
        if (Objects.nonNull(responsibleUser)) {
            query.setParameter("responsibleUser", responsibleUser);
        }
    }

    private List<SimpleUserdocRegNumberChangeLog> fillResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        return resultList.stream()
                .map(object ->
                        new SimpleUserdocRegNumberChangeLog(
                                (String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue(), (String) object[4], (String) object[5], (Date) object[6], (String) object[7]
                        ))
                .collect(Collectors.toList());
    }
}
