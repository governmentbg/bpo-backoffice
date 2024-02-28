package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.core.mapper.error.ErrorLogMapper;
import bg.duosoft.ipas.core.model.error.CErrorLog;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpErrorLog;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpErrorLogRepositoryCustom;
import bg.duosoft.ipas.util.filter.ErrorLogFilter;
import bg.duosoft.ipas.util.filter.sorter.ErrorLogSorterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class IpErrorLogRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpErrorLogRepositoryCustom {

    @Autowired
    private ErrorLogMapper errorLogMapper;

    @Override
    public List<CErrorLog> selectErrorLogs(ErrorLogFilter filter) {
        String buildQuery = buildQuery(filter, false);
        TypedQuery<IpErrorLog> query = em.createQuery(buildQuery, IpErrorLog.class);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillResult(query);
    }

    @Override
    public int selectErrorLogsCount(ErrorLogFilter filter) {
        String buildQuery = buildQuery(filter, true);
        TypedQuery<Number> query = em.createQuery(buildQuery, Number.class);
        addQueryParams(filter, query);
        Number result = query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(ErrorLogFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(e) " : " e ")
                .append(" FROM IpErrorLog e ")
                .append(" WHERE e.needManualFix = true ");

        boolean isResolved = filter.isResolved();
        if (isResolved)
            queryBuilder.append(" AND e.dateResolved is not null ");
        else
            queryBuilder.append(" AND e.dateResolved is null ");

        if (!StringUtils.isEmpty(filter.getPriority()))
            queryBuilder.append(" AND e.priority = :priority ");

        if (!StringUtils.isEmpty(filter.getAbout()))
            queryBuilder.append(" AND e.about = :about ");

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = ErrorLogSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        return queryBuilder.toString();
    }

    private void addQueryParams(ErrorLogFilter filter, Query query) {
        String priority = filter.getPriority();
        if (!StringUtils.isEmpty(priority))
            query.setParameter("priority", priority);

        String about = filter.getAbout();
        if (!StringUtils.isEmpty(about))
            query.setParameter("about", about);
    }

    private List<CErrorLog> fillResult(TypedQuery<IpErrorLog> query) {
        List<IpErrorLog> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        return errorLogMapper.toCoreList(resultList);
    }

}
