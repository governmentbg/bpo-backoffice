package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectHomePanelResult;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.MyObjectsHomePanelRepository;
import bg.duosoft.ipas.persistence.repository.nonentity.MyObjectsRepository;
import bg.duosoft.ipas.util.filter.MyObjectsHomePanelFilter;
import bg.duosoft.ipas.util.filter.sorter.MyObjectsSorterUtils;
import bg.duosoft.ipas.util.home_page.HomePageUtils;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class MyObjectsHomeRepositoryImpl extends BaseRepositoryCustomImpl implements MyObjectsHomePanelRepository {

    @Autowired
    private UserService userService;

    @Override
    public List<IPObjectHomePanelResult> getMyObjectsList(MyObjectsHomePanelFilter filter) {
        String buildQuery = buildQuery(filter, false,false);
        Query query = em.createNativeQuery(buildQuery)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(IPObjectHomePanelResult.class));
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());

        return query.getResultList();
    }

    @Override
    public Integer getMyObjectsCount(MyObjectsHomePanelFilter filter) {
       return getCount(filter,true,false);
    }

    @Override
    public Integer getNewlyAllocatedObjectsCount(MyObjectsHomePanelFilter filter) {
        return getCount(filter,true,true);
    }

    private Integer getCount(MyObjectsHomePanelFilter filter, boolean isCount,boolean isNewlyAllocatedCount){
        String buildQuery = buildQuery(filter, isCount,isNewlyAllocatedCount);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(MyObjectsHomePanelFilter filter, boolean isCount,boolean isNewlyAllocatedCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) " : " prc.file_seq as fileSeq, prc.file_typ as fileTyp,  cast(prc.file_ser as int) as fileSer, cast(prc.FILE_NBR as int) as fileNbr, cfs.STATUS_NAME as description\n");
        queryBuilder.append(" FROM ipasprod.IP_PROC prc \n");
        queryBuilder.append(" JOIN IP_FILE fl on fl.PROC_NBR = prc.PROC_NBR and fl.PROC_TYP = prc.PROC_TYP \n");
        queryBuilder.append(" JOIN ipasprod.CF_STATUS cfs on prc.STATUS_CODE = cfs.STATUS_CODE and cfs.PROC_TYP = prc.PROC_TYP  \n");
        queryBuilder.append(" LEFT JOIN EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ch on ch.PROC_NBR = prc.PROC_NBR and ch.PROC_TYP = prc.PROC_TYP \n");
        queryBuilder.append(" WHERE fl.FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') ");;
        queryBuilder.append(" AND prc.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        queryBuilder.append(" and ((ch.STATUS = 0 and ch.NEW_RESPONSIBLE_USER_ID  =  prc.RESPONSIBLE_USER_ID )");
        if (!isNewlyAllocatedCount){
            queryBuilder.append(" or ((ch.PROC_NBR is null)" +
                    " or (ch.DATE_CHANGED = ( select MAX(ch2.DATE_CHANGED) from ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES ch2" +
                    " where ch2.PROC_NBR=ch.PROC_NBR and ch2.PROC_TYP=ch.PROC_TYP )) ) ");
        }
        queryBuilder.append(") ");


        if (!CollectionUtils.isEmpty(filter.getFileTypes())){
            queryBuilder.append(" AND fl.file_typ in (:fileTypes)  ");
        }


        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = MyObjectsSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        return queryBuilder.toString();
    }

    private void addQueryParams(MyObjectsHomePanelFilter filter, Query query) {
        query.setParameter("responsibleUsers", HomePageUtils.getResponsibleUsersForMyObjectPanels(userService,filter.getResponsibleUser()));

        if (!CollectionUtils.isEmpty(filter.getFileTypes())){
            query.setParameter("fileTypes", filter.getFileTypes());
        }
    }
}
