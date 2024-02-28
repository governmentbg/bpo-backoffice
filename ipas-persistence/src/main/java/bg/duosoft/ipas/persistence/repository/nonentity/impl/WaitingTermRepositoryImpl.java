package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.WaitingTermsPanelConfig;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.WaitingTermRepository;
import bg.duosoft.ipas.util.filter.WaitingTermFilter;
import bg.duosoft.ipas.util.filter.sorter.WaitingTermSorterUtils;
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
public class WaitingTermRepositoryImpl extends BaseRepositoryCustomImpl implements WaitingTermRepository {

    @Autowired
    private UserService userService;

    @Override
    public List<IPObjectSimpleResult> getWaitingTermList(WaitingTermFilter filter) {
        String buildQuery = null;

        if (filter.getPanelType().equals(WaitingTermsPanelConfig.ALL.code())) {
            buildQuery = buildQueryOnAllPanelType(filter, false);
        }

        if (filter.getPanelType().equals(WaitingTermsPanelConfig.ZMR.code())) {
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
                    String status = (String) objects[1];
                    String statusCode = (String) objects[2];
                    Date expirationDate = (Date) objects[3];
                    String[] filingNumberParts = filingNumber.split("/");
                    return new IPObjectSimpleResult(
                            filingNumberParts[0], filingNumberParts[1], Integer.valueOf(filingNumberParts[2]),
                            Integer.valueOf(filingNumberParts[3]), null, null, statusCode, status,
                            null, expirationDate, null, filingNumber, null, null, null, (String) objects[4], null, null, null, null, null, null, null, null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getWaitingTermCount(WaitingTermFilter filter) {
        String buildQuery = null;

        if (filter.getPanelType().equals(WaitingTermsPanelConfig.ALL.code())) {
            buildQuery = buildQueryOnAllPanelType(filter, true);
        }

        if (filter.getPanelType().equals(WaitingTermsPanelConfig.ZMR.code())) {
            buildQuery = buildQueryOnZmrPanelType(filter, true);
        }

        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public Map<String, String> getStatuses(WaitingTermFilter filter) {
        StringBuilder queryBuilder = new StringBuilder(" select s.STATUS_CODE as code,s.status_name as name" +
                " from CF_STATUS s");
        queryBuilder.append(" inner join ip_proc p on s.STATUS_CODE = p.STATUS_CODE and p.PROC_TYP = s.PROC_TYP ");

        if (filter.getPanelType().equals(WaitingTermsPanelConfig.ALL.code())) {
            queryBuilder.append(" WHERE p.EXPIRATION_DATE is not null AND p.file_nbr is not null AND p.file_nbr <> 0\n" +
                    " AND p.status_code not in ('015', '034', '987', '1032', '199', '222', '215', '346', '290', '272', '254', '773', '286', '189', '287', '201','348','363','139','384','389')\n" +
                    " AND p.proc_typ not in (8) ");
            String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
            if (!additionalConditionsRelatedToRoles.isEmpty()) {
                queryBuilder.append(additionalConditionsRelatedToRoles);
            }
        }

        if (filter.getPanelType().equals(WaitingTermsPanelConfig.ZMR.code())) {
            queryBuilder.append(" where p.doc_nbr is not null and p.USERDOC_TYP='ЗМР' and p.EXPIRATION_DATE is not null ");
        }


        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }

        Query query = em.createNativeQuery(queryBuilder.toString());

        if (Objects.nonNull(filter.getResponsibleUser())) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(filter.getResponsibleUser()));
        }


        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String,String> statuses = new LinkedHashMap<>();
        for (Object[] objects: resultList) {
            String code =(String) objects[0];
            String name =(String) objects[1];
            if (!statuses.containsKey(code)){
                statuses.put(code,name);
            }
        }
        return statuses;
    }

    private String constructConditionsRelatedToRoles() {
        StringBuilder queryBuilder = new StringBuilder();
        if (!SecurityUtils.hasRights(SecurityRole.MarkViewOwn)) {
            queryBuilder.append(" AND p.FILE_TYP not in (" + BasicUtils.getMarkRelatedFileTypesAsSequence() + ") ");
        }
        if (!SecurityUtils.hasRights(SecurityRole.PatentViewOwn)) {
            queryBuilder.append(" AND p.FILE_TYP not in (" + BasicUtils.getPatentRelatedFileTypesAsSequence() + ") ");
        }
        return queryBuilder.toString();
    }

    private String buildQueryOnZmrPanelType(WaitingTermFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " c.* ")
                .append("FROM (")
                .append("select top 500 f.file_seq + '/' + f.file_typ + '/' + CAST(f.file_ser as VARCHAR(4)) + '/' +\n" +
                        "       CAST(f.FILE_NBR as VARCHAR)     as filingNumber,\n" +
                        "       s.STATUS_NAME                  as status,\n" +
                        "       s.STATUS_CODE                  as statusCode,\n" +
                        "       cast(p.EXPIRATION_DATE as date) as expirationDate,\n" +
                        "       us.USER_NAME                    as userName\n" +
                        "from ipasprod.ip_proc p\n" +
                        "         join ipasprod.CF_USERDOC_TYPE udt on p.USERDOC_TYP = udt.USERDOC_TYP\n" +
                        "         INNER JOIN CF_STATUS s on s.STATUS_CODE = p.STATUS_CODE and p.PROC_TYP = s.PROC_TYP \n" +
                        "         JOIN IPASPROD.ip_doc d\n" +
                        "              ON p.DOC_ORI = d.doc_ori AND p.DOC_LOG = d.doc_log AND p.doc_ser = d.doc_ser AND p.doc_nbr = d.DOC_NBR\n" +
                        "         LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID\n" +
                        "         JOIN IPASPROD.IP_FILE f on f.FILE_NBR = p.USERDOC_FILE_NBR and f.FILE_SER = p.USERDOC_FILE_SER and\n" +
                        "                                    f.FILE_TYP = p.USERDOC_FILE_TYP and f.FILE_SEQ = p.USERDOC_FILE_SEQ\n" +
                        "where p.doc_nbr is not null and p.USERDOC_TYP='ЗМР' and p.EXPIRATION_DATE is not null and p.expiration_Date>getdate() \n");


        if (!StringUtils.isEmpty(filter.getFileType())) {
            queryBuilder.append(" AND f.FILE_TYP in (:fileTypes) ");
        }

        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            queryBuilder.append(" AND s.STATUS_CODE = :statusCode ");
        }

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = WaitingTermSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        queryBuilder.append(" ) c ");
        return queryBuilder.toString();
    }


    private String buildQueryOnAllPanelType(WaitingTermFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " c.* ")
                .append(" FROM ( select top 500 p.file_seq + '/' + p.file_typ + '/' + CAST(p.file_ser as VARCHAR(4)) + '/' +\n" +
                        "       CAST(p.FILE_NBR as VARCHAR) as filingNumber,\n" +
                        "       s.STATUS_NAME                                    as status,\n" +
                        "       s.STATUS_CODE                                    as statusCode,\n" +
                        "       cast(p.EXPIRATION_DATE as date)                  as expirationDate, us.USER_NAME as userName ")
                .append(" FROM ipasprod.IP_PROC p ")
                .append(" LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID \n")
                .append(" JOIN ipasprod.CF_STATUS s on p.STATUS_CODE = s.STATUS_CODE and p.PROC_TYP = s.PROC_TYP ")
                .append(" WHERE 1=1 ")
                .append(" AND p.EXPIRATION_DATE is not null ")
                .append(" AND p.file_nbr is not null ")
                .append(" AND p.file_nbr <> 0 ")
                .append(" AND p.status_code not in\n" +
                        "      ('015', '034', '987', '1032', '199', '222', '215', '346', '290', '272', '254', '773', '286', '189', '287', '201','348','363','139','384','389') ")
                .append(" AND p.proc_typ not in (8) ").append(" and p.FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') ");

        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()) {
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }

        if (!StringUtils.isEmpty(filter.getFileType())) {
            queryBuilder.append(" AND p.FILE_TYP in (:fileTypes) ");
        }

        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            queryBuilder.append(" AND s.STATUS_CODE = :statusCode ");
        }

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = WaitingTermSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        queryBuilder.append(" ) c ");
        return queryBuilder.toString();
    }

    private void addQueryParams(WaitingTermFilter filter, Query query) {
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
    }

}
