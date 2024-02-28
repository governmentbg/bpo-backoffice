package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.core.mapper.common.StringToBooleanMapper;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfUserdocType;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.CfUserdocTypeRepositoryCustom;
import bg.duosoft.ipas.util.filter.UserdocTypesFilter;
import bg.duosoft.ipas.util.filter.sorter.UserdocTypesSorterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;

@Repository
public class CfUserdocTypeRepositoryCustomImpl extends BaseRepositoryCustomImpl implements CfUserdocTypeRepositoryCustom {

    @Autowired
    private StringToBooleanMapper stringToBooleanMapper;

    @Override
    public List<CfUserdocType> getUserdocTypes(UserdocTypesFilter filter) {
        String buildQuery = buildQuery(filter, false);
        TypedQuery<CfUserdocType> query = em.createQuery(buildQuery, CfUserdocType.class);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return query.getResultList();
    }

    @Override
    public Integer getUserdocTypesCount(UserdocTypesFilter filter) {
        String buildQuery = buildQuery(filter, true);
        TypedQuery<Number> query = em.createQuery(buildQuery, Number.class);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(UserdocTypesFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT")
                .append(isCount ? " COUNT(ut)" : " ut")
                .append(" FROM CfUserdocType ut")
                .append(" WHERE 1=1");

        if (!StringUtils.isEmpty(filter.getUserdocName())) {
            queryBuilder.append(" AND lower(ut.userdocName) LIKE :userdocName");
        }
        if (Objects.nonNull(filter.getIndInactive())) {
            queryBuilder.append(" AND ut.indInactive = :status");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = UserdocTypesSorterUtils.userdocTypesSorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        return queryBuilder.toString();
    }

    private void addQueryParams(UserdocTypesFilter filter, Query query) {
        String userdocName = filter.getUserdocName();
        if (!StringUtils.isEmpty(userdocName)) {
            query.setParameter("userdocName", "%" + userdocName.toLowerCase().trim() + "%");
        }

        Boolean status = filter.getIndInactive();
        if (Objects.nonNull(status)) {
                query.setParameter("status", stringToBooleanMapper.booleanToString(status));
        }
    }

}
