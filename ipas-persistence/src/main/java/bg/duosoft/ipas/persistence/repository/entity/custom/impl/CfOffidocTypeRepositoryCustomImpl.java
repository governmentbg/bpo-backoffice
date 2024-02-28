package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfOffidocType;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.CfOffidocTypeRepositoryCustom;
import bg.duosoft.ipas.util.filter.OffidocTypeFilter;
import bg.duosoft.ipas.util.filter.sorter.OffidocTypeSorterUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class CfOffidocTypeRepositoryCustomImpl extends BaseRepositoryCustomImpl implements CfOffidocTypeRepositoryCustom {

    @Override
    public List<CfOffidocType> selectOffidocTypes(OffidocTypeFilter filter) {
        String buildQuery = buildQuery(filter, false);
        TypedQuery<CfOffidocType> query = em.createQuery(buildQuery, CfOffidocType.class);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return query.getResultList();
    }

    @Override
    public Integer selectOffidocTypesCount(OffidocTypeFilter filter) {
        String buildQuery = buildQuery(filter, true);
        TypedQuery<Number> query = em.createQuery(buildQuery, Number.class);
        addQueryParams(filter, query);
        Number result = query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(OffidocTypeFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT")
                .append(isCount ? " COUNT(ot)" : " ot")
                .append(" FROM CfOffidocType ot")
                .append(" WHERE 1=1");

        if (!StringUtils.isEmpty(filter.getOffidocName())) {
            queryBuilder.append(" AND lower(ot.offidocName) LIKE :offidocName");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = OffidocTypeSorterUtils.offidocTypeSorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        return queryBuilder.toString();
    }

    private void addQueryParams(OffidocTypeFilter filter, Query query) {
        String offidocName = filter.getOffidocName();
        if (!StringUtils.isEmpty(offidocName)) {
            query.setParameter("offidocName", "%" + offidocName.toLowerCase().trim() + "%");
        }
    }
}
