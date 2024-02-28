package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.ActionTypeKind;
import bg.duosoft.ipas.enums.ExpiredTermsPanelConfig;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.ExpiredTermRepository;
import bg.duosoft.ipas.util.filter.ExpiredTermFilter;
import bg.duosoft.ipas.util.filter.sorter.ExpiredTermSortedUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ExpiredTermRepositoryImpl extends BaseRepositoryCustomImpl implements ExpiredTermRepository {

    @Autowired
    private UserService userService;

    @Override
    public List<IPObjectSimpleResult> getExpiredTermsList(ExpiredTermFilter filter) {
        String buildQuery = null;
        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ALL.code())) {
            buildQuery = buildQueryOnAllPanelType(filter, false);
        }

        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ZMR.code())) {
            buildQuery = buildQueryOnZmrPanelType(filter, false);
        }

        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(objects -> {
                    String filingNumber = (String) objects[0];
                    String[] filingNumberParts = filingNumber.split("/");
                    return new IPObjectSimpleResult(
                            filingNumberParts[0], filingNumberParts[1], Integer.valueOf(filingNumberParts[2]),
                            Integer.valueOf(filingNumberParts[3]), null, null, null, (String) objects[5],
                            null, null, (Date) objects[2], filingNumber, (String) objects[1], null, (String) objects[3], (String) objects[4], null, null, null, null, null, null, null, null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getExpiredTersmCount(ExpiredTermFilter filter) {
        String buildQuery = null;
        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ALL.code())) {
            buildQuery = buildQueryOnAllPanelType(filter, true);
        }

        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ZMR.code())) {
            buildQuery = buildQueryOnZmrPanelType(filter, true);
        }

        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public List<CActionType> getExpiredActionTypes(ExpiredTermFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();

        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ALL.code())) {
            queryBuilder.append(" select cfa.ACTION_TYP,cfa.ACTION_TYPE_NAME\n" +
                    "FROM ipasprod.IP_ACTION a\n" +
                    "join ipasprod.CF_ACTION_TYPE cfa on a.ACTION_TYP = cfa.ACTION_TYP\n" +
                    "join ipasprod.ip_proc prc on a.PROC_NBR = prc.PROC_NBR and a.PROC_TYP = prc.PROC_TYP\n" +
                    "where cfa.AUTOMATIC_ACTION_WCODE= 1  and prc.file_seq is not null and prc.PROC_TYP = prc.FILE_PROC_TYP and  prc.PROC_NBR = prc.FILE_PROC_NBR and prc.file_typ is not null and prc.file_ser is not null and prc.FILE_NBR is not null  and" +
                    " prc.FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "')" +
                    " and a.action_date>dateadd(month, -6, current_timestamp) ");

            String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
            if (!additionalConditionsRelatedToRoles.isEmpty()) {
                queryBuilder.append(additionalConditionsRelatedToRoles);
            }
        }

        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ZMR.code())) {
            queryBuilder.append(" select cfa.ACTION_TYP,cfa.ACTION_TYPE_NAME\n" +
                    "FROM ipasprod.IP_ACTION a\n" +
                    "join ipasprod.CF_ACTION_TYPE cfa on a.ACTION_TYP = cfa.ACTION_TYP\n" +
                    " join ipasprod.ip_proc p on a.PROC_NBR = p.PROC_NBR and a.PROC_TYP = p.PROC_TYP " +
                    "join ipasprod.ip_proc prc on a.PROC_NBR = prc.PROC_NBR and a.PROC_TYP = prc.PROC_TYP\n" +
                    "where cfa.AUTOMATIC_ACTION_WCODE=1 and p.USERDOC_TYP='ЗМР' and p.doc_nbr is not null and a.NEW_STATUS_CODE = p.STATUS_CODE\n" +
                    " ");
        }


        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND prc.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }
        queryBuilder.append(" group by cfa.ACTION_TYP,cfa.ACTION_TYPE_NAME ");
        queryBuilder.append(" order by cfa.ACTION_TYPE_NAME");
        Query query = em.createNativeQuery(queryBuilder.toString());

        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        List<CActionType> actionTypes = new ArrayList<>();
        for (Object[] objects : resultList) {
            CActionType cActionType = new CActionType();
            cActionType.setActionType((String) objects[0]);
            cActionType.setActionName((String) objects[1]);
            actionTypes.add(cActionType);
        }
        return actionTypes;
    }

    @Override
    public Map<String, String> getStatuses(ExpiredTermFilter filter) {

        StringBuilder queryBuilder = new StringBuilder(" select cs.STATUS_CODE as code,cs.status_name as name" +
                " FROM ipasprod.IP_ACTION a\n" +
                "join ipasprod.CF_ACTION_TYPE cfa on a.ACTION_TYP = cfa.ACTION_TYP\n" +
                "join ipasprod.ip_proc prc on a.PROC_NBR = prc.PROC_NBR and a.PROC_TYP = prc.PROC_TYP\n" +
                "JOIN CF_STATUS cs on cs.STATUS_CODE = prc.STATUS_CODE and prc.PROC_TYP = cs.PROC_TYP ");
        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ALL.code())) {
            queryBuilder.append(" where cfa.AUTOMATIC_ACTION_WCODE= " + ActionTypeKind.AUTOMATIC.code() + " ");
            queryBuilder.append(" and prc.file_seq is not null and " +
                    "prc.PROC_TYP = prc.FILE_PROC_TYP and  prc.PROC_NBR = prc.FILE_PROC_NBR and " +
                    "prc.file_typ is not null and " +
                    "prc.file_ser is not null and " +
                    "prc.FILE_NBR is not null ").append(" and prc.FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "')" +
                    " and a.action_date>dateadd(month, -6, current_timestamp) ");

            String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
            if (!additionalConditionsRelatedToRoles.isEmpty()) {
                queryBuilder.append(additionalConditionsRelatedToRoles);
            }
        }

        if (filter.getPanelType().equals(ExpiredTermsPanelConfig.ZMR.code())) {
            queryBuilder.append(" where cfa.AUTOMATIC_ACTION_WCODE=1 and prc.USERDOC_TYP='ЗМР' and prc.doc_nbr is not null and a.NEW_STATUS_CODE = prc.STATUS_CODE ");
        }

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND prc.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }

        Query query = em.createNativeQuery(queryBuilder.toString());

        Integer responsibleUser = filter.getResponsibleUser();
        if (Objects.nonNull(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String, String> statuses = new LinkedHashMap<>();
        for (Object[] objects : resultList) {
            String code = (String) objects[0];
            String name = (String) objects[1];
            if (!statuses.containsKey(code)) {
                statuses.put(code, name);
            }
        }
        return statuses;
    }


    private String buildQueryOnZmrPanelType(ExpiredTermFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " c.*  ")
                .append("FROM (")
                .append("select top 500 f.file_seq + '/' + f.file_typ + '/' + CAST(f.file_ser as VARCHAR(4)) + '/' + CAST(f.FILE_NBR as VARCHAR) as filingNumber,\n" +
                        "       cfa.ACTION_TYPE_NAME                                as actionTypeName,\n" +
                        "       cast(a.ACTION_DATE as date)                        as actionDate,\n" +
                        "       cfa.ACTION_TYP                                     as actionType, us.USER_NAME as userName, cs.STATUS_NAME as statusName\n" +
                        "from ipasprod.ip_action a\n" +
                        "join ipasprod.CF_ACTION_TYPE cfa on a.ACTION_TYP = cfa.ACTION_TYP\n" +
                        "join ipasprod.ip_proc p on a.PROC_NBR = p.PROC_NBR and a.PROC_TYP = p.PROC_TYP\n" +
                        "INNER JOIN CF_STATUS cs on cs.STATUS_CODE = p.STATUS_CODE and cs.PROC_TYP = p.PROC_TYP JOIN IPASPROD.ip_doc d ON p.DOC_ORI = d.doc_ori AND p.DOC_LOG = d.doc_log AND p.doc_ser = d.doc_ser AND p.doc_nbr = d.DOC_NBR\n" +
                        "LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID\n" +
                        "JOIN IPASPROD.IP_FILE f on f.FILE_NBR=p.USERDOC_FILE_NBR and f.FILE_SER = p.USERDOC_FILE_SER and f.FILE_TYP = p.USERDOC_FILE_TYP and f.FILE_SEQ = p.USERDOC_FILE_SEQ\n" +
                        "where cfa.AUTOMATIC_ACTION_WCODE=1 and p.USERDOC_TYP='ЗМР' and p.doc_nbr is not null and a.NEW_STATUS_CODE = p.STATUS_CODE \n" +
                        "");

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }

        if (!StringUtils.isEmpty(filter.getActionType())) {
            queryBuilder.append(" AND a.ACTION_TYP = :actionTyp ");
        }

        if (!StringUtils.isEmpty(filter.getFileType())) {
            queryBuilder.append(" AND f.FILE_TYP in (:fileTypes) ");
        }

        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            queryBuilder.append(" AND cs.STATUS_CODE = :statusCode ");
        }


        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = ExpiredTermSortedUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        queryBuilder.append(" ) c ");
        return queryBuilder.toString();
    }


    private String buildQueryOnAllPanelType(ExpiredTermFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " c.*  ");
        queryBuilder.append(" FROM ( select TOP 500 prc.file_seq + '/' + prc.file_typ + '/' + CAST(prc.file_ser as VARCHAR(4)) + '/' +\n" +
                "       CAST(prc.FILE_NBR as VARCHAR) as filingNumber,\n" +
                "       cfa.ACTION_TYPE_NAME                                as actionTypeName,\n" +
                "       cast(a.ACTION_DATE as date)                        as actionDate,\n" +
                "       cfa.ACTION_TYP                                     as actionType, us.USER_NAME as userName , cs.STATUS_NAME as statusName ");
        queryBuilder.append(" FROM ipasprod.IP_ACTION a ");
        queryBuilder.append(" join ipasprod.CF_ACTION_TYPE cfa on a.ACTION_TYP = cfa.ACTION_TYP");
        queryBuilder.append(" join ipasprod.ip_proc prc on a.PROC_NBR = prc.PROC_NBR and a.PROC_TYP = prc.PROC_TYP ");
        queryBuilder.append(" LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = prc.RESPONSIBLE_USER_ID \n");
        queryBuilder.append(" JOIN CF_STATUS cs on cs.STATUS_CODE = prc.STATUS_CODE and cs.PROC_TYP = prc.PROC_TYP  ");
        queryBuilder.append(" where cfa.AUTOMATIC_ACTION_WCODE= " + ActionTypeKind.AUTOMATIC.code() + " ");
        queryBuilder.append(" and prc.file_seq is not null and " +
                "prc.PROC_TYP = prc.FILE_PROC_TYP and  prc.PROC_NBR = prc.FILE_PROC_NBR and " +
                "prc.file_typ is not null and " +
                "prc.file_ser is not null and " +
                "prc.FILE_NBR is not null ").append(" and prc.FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "')" +
                " and a.action_date>dateadd(month, -6, current_timestamp) ");

        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()) {
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND prc.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }
        if (!StringUtils.isEmpty(filter.getActionType())) {
            queryBuilder.append(" AND a.ACTION_TYP = :actionTyp ");
        }

        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            queryBuilder.append(" AND cs.STATUS_CODE = :statusCode ");
        }

        if (!StringUtils.isEmpty(filter.getFileType())) {
            queryBuilder.append(" AND prc.FILE_TYP in (:fileTypes) ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = ExpiredTermSortedUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }
        queryBuilder.append(" ) c ");
        return queryBuilder.toString();
    }

    private void addQueryParams(ExpiredTermFilter filter, Query query) {
        String fileType = filter.getFileType();
        if (!StringUtils.isEmpty(fileType)) {
            query.setParameter("fileTypes", FileType.getLinkedFileTypes(fileType));
        }
        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            query.setParameter("statusCode", filter.getStatusCode());
        }
        Integer responsibleUser = filter.getResponsibleUser();
        if (Objects.nonNull(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }
        if (!StringUtils.isEmpty(filter.getActionType())) {
            query.setParameter("actionTyp", filter.getActionType());
        }
    }

    private String constructConditionsRelatedToRoles() {
        StringBuilder queryBuilder = new StringBuilder();
        if (!SecurityUtils.hasRights(SecurityRole.MarkViewOwn)) {
            queryBuilder.append(" AND prc.FILE_TYP not in (" + BasicUtils.getMarkRelatedFileTypesAsSequence() + ") ");
        }
        if (!SecurityUtils.hasRights(SecurityRole.PatentViewOwn)) {
            queryBuilder.append(" AND prc.FILE_TYP not in (" + BasicUtils.getPatentRelatedFileTypesAsSequence() + ") ");
        }
        return queryBuilder.toString();
    }
}
