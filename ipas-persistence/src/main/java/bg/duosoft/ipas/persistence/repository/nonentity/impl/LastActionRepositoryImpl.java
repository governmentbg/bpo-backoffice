package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.service.nomenclature.FileTypeService;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.ActionTypeKind;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.LastActionsResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.LastActionRepository;
import bg.duosoft.ipas.util.filter.LastActionFilter;
import bg.duosoft.ipas.util.filter.sorter.UserdocCorrespondenceTermsSortedUtils;
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
public class LastActionRepositoryImpl extends BaseRepositoryCustomImpl implements LastActionRepository {

    @Autowired
    private UserService userService;

    @Autowired
    private FileTypeService fileTypeService;

    @Override
    public List<LastActionsResult> getLastActionList(LastActionFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);

        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillSimpleRequestResult(query);
    }

    @Override
    public int getLastActionCount(LastActionFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public Map<String, String> getLastActionFileTypes(LastActionFilter filter) {
        return fileTypeService.getFileTypesMapBasedOnSecurityRights();
    }

    @Override
    public Map<String, String> getLastActionUserdocTypes(LastActionFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select DISTINCT ut.USERDOC_TYP as type, ut.USERDOC_NAME as name\n" +
                "from ipasprod.IP_PROC p\n" +
                "LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID\n" +
                "join ipasprod.IP_ACTION a on p.PROC_NBR = a.PROC_NBR and p.PROC_TYP = a.PROC_TYP\n" +
                "JOIN IPASPROD.CF_USERDOC_TYPE ut on p.USERDOC_TYP = ut.USERDOC_TYP\n" +
                "where COALESCE(p.file_seq + '/' + p.file_typ + '/' + CAST(p.file_ser as VARCHAR(4)) + '/' +\n" +
                "                     CAST(p.FILE_NBR as VARCHAR),\n" +
                "                     p.DOC_ORI + '/' + p.DOC_LOG + '/' + CAST(p.DOC_SER as VARCHAR(4)) + '/' +\n" +
                " CAST(p.DOC_NBR as VARCHAR)) is not null  and ( p.FILE_TYP not in('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') or p.FILE_TYP is null) " +
                " and a.action_date>dateadd(month, -6, current_timestamp) ");
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }
        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()) {
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }
        queryBuilder.append(" order by ut.USERDOC_NAME ");
        Query query = em.createNativeQuery(queryBuilder.toString());

        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String, String> userdocTypes = new LinkedHashMap<>();
        for (Object[] objects : resultList) {
            String userdocTyp = (String) objects[0];
            String userdocTypName = (String) objects[1];
            userdocTypes.put(userdocTyp, userdocTypName);
        }
        return userdocTypes;
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

    private String buildQuery(LastActionFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " c.* ")
                .append(" FROM ( select top 500 COALESCE(p.file_seq + '/' + p.file_typ + '/' + CAST(p.file_ser as VARCHAR(4)) + '/' +\n" +
                        "                              CAST(p.FILE_NBR as VARCHAR),\n" +
                        "                              p.userdoc_file_seq + '/' + p.userdoc_file_typ + '/' + CAST(p.userdoc_file_ser as VARCHAR(4)) + '/' +\n" +
                        "                              CAST(p.userdoc_FILE_NBR as VARCHAR)) as filingNumber,\n" +
                        "                 s.STATUS_NAME                                                 as status,\n" +
                        "                 s.STATUS_CODE                                                 as statusCode,\n" +
                        "                 a.ACTION_DATE                                                 as actionDate,\n" +
                        "                 at.ACTION_TYPE_NAME                                           as actionName, us.USER_NAME as userName,\n" +
                        " COALESCE(d.EXTERNAL_SYSTEM_ID,CONCAT(p.DOC_ORI, p.DOC_LOG,p.DOC_SER,p.DOC_NBR)) as documentNumber, " +
                        " utd.USERDOC_NAME as documentTypeName, us2.USER_NAME as captureUserName " +
                        "      from ipasprod.IP_PROC p\n" +
                        " LEFT JOIN IPASPROD.ip_doc d ON p.DOC_ORI = d.doc_ori AND p.DOC_LOG = d.doc_log AND p.doc_ser = d.doc_ser AND p.doc_nbr = d.DOC_NBR " +
                        "  left join CF_USERDOC_TYPE utd on utd.USERDOC_TYP = p.USERDOC_TYP " +
                        "      LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID \n" +
                        "             join ipasprod.IP_ACTION a on p.PROC_NBR = a.PROC_NBR and p.PROC_TYP = a.PROC_TYP\n" +
                        "             join ipasprod.CF_STATUS s on p.STATUS_CODE = s.STATUS_CODE and p.PROC_TYP = s.PROC_TYP \n" +
                        "             join IPASPROD.CF_ACTION_TYPE at on a.ACTION_TYP = at.ACTION_TYP\n" +
                        "             join IPASPROD.IP_USER us2 ON us2.USER_ID = a.CAPTURE_USER_ID " +
                        "      where COALESCE(p.file_seq + '/' + p.file_typ + '/' + CAST(p.file_ser as VARCHAR(4)) + '/' +\n" +
                        "                     CAST(p.FILE_NBR as VARCHAR),\n" +
                        "                     p.DOC_ORI + '/' + p.DOC_LOG + '/' + CAST(p.DOC_SER as VARCHAR(4)) + '/' +\n" +
                        "                     CAST(p.DOC_NBR as VARCHAR)) is not null ").append(" and (p.FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') or p.FILE_TYP is null) " +
                        "" +
                        "  and a.action_date>dateadd(month, -6, current_timestamp)  ");
        ;


        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()) {
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }

        Integer actionTypeKind = filter.getActionTypeKind();
        if (ActionTypeKind.AUTOMATIC.code() == actionTypeKind) {
            queryBuilder.append(" AND at.AUTOMATIC_ACTION_WCODE is not null ");
        } else if (ActionTypeKind.MANUAL.code() == actionTypeKind) {
            queryBuilder.append(" AND at.AUTOMATIC_ACTION_WCODE is null ");
        }
        if (!StringUtils.isEmpty(filter.getFileType())) {
            queryBuilder.append(" AND (p.FILE_TYP in (:fileTypes) OR p.userdoc_file_typ in (:fileTypes) ) ");
        }
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }
        if (!StringUtils.isEmpty(filter.getUserdocType())) {
            queryBuilder.append(" AND p.USERDOC_TYP = :userdocType ");
        }
        if (!StringUtils.isEmpty(filter.getCaptureUser())) {
            queryBuilder.append(" AND us2.USER_ID = :captureUser ");
        }
        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()) {
            queryBuilder.append(" AND (d.EXTERNAL_SYSTEM_ID like :userdocFilingNumber or " +
                    " CONCAT(p.DOC_ORI, p.DOC_LOG,p.DOC_SER,p.DOC_NBR) like :userdocFilingNumber)  ");
        }
        if (!StringUtils.isEmpty(filter.getObjectFileNbr())) {
            queryBuilder.append(" AND (p.FILE_NBR like :objectFileNbr or p.userdoc_file_nbr like :objectFileNbr) ");
        }

        if (Objects.nonNull(filter.getActionDateFrom())) {
            queryBuilder.append(" AND a.action_date >= :actionDateFrom ");
        }

        if (Objects.nonNull(filter.getActionDateTo())) {
            queryBuilder.append(" AND a.action_date <= :actionDateTo ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            if (Objects.nonNull(sortColumn)) {
                String[] columns = UserdocCorrespondenceTermsSortedUtils.sorterColumnMap().get(sortColumn).split(",");
                String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
                queryBuilder.append(" ORDER BY ").append(order);
            } else {
                queryBuilder.append(" order by ACTION_DATE desc ");
            }
        }

        queryBuilder.append(" ) c ");

        return queryBuilder.toString();
    }

    private void addQueryParams(LastActionFilter filter, Query query) {
        String fileType = filter.getFileType();
        if (!StringUtils.isEmpty(fileType)) {
            query.setParameter("fileTypes", FileType.getLinkedFileTypes(fileType));
        }

        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()) {
            query.setParameter("userdocFilingNumber", "%" + filter.getUserdocFilingNumber() + "%");
        }

        if (!StringUtils.isEmpty(filter.getObjectFileNbr())) {
            query.setParameter("objectFileNbr", "%" + filter.getObjectFileNbr() + "%");
        }

        if (!StringUtils.isEmpty(filter.getUserdocType())) {
            query.setParameter("userdocType", filter.getUserdocType());
        }

        if (!StringUtils.isEmpty(filter.getCaptureUser())) {
            query.setParameter("captureUser", filter.getCaptureUser());
        }

        if (Objects.nonNull(filter.getActionDateFrom())) {
            query.setParameter("actionDateFrom", filter.getActionDateFrom());
        }

        if (Objects.nonNull(filter.getActionDateTo())) {
            query.setParameter("actionDateTo", filter.getActionDateTo());
        }

        Integer responsibleUser = filter.getResponsibleUser();
        if (Objects.nonNull(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }
    }

    public static List<LastActionsResult> fillSimpleRequestResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(objects -> {
                    String filingNumber = (String) objects[0];
                    String[] filingNumberParts = filingNumber.split("/");
                    return new LastActionsResult(
                            filingNumberParts[0], filingNumberParts[1], Integer.valueOf(filingNumberParts[2]),
                            Integer.valueOf(filingNumberParts[3]), (String) objects[2], (String) objects[1],
                            (Date) objects[3], filingNumber, (String) objects[4], (String) objects[5], (String) objects[6], (String) objects[7], (String) objects[8]);
                })
                .collect(Collectors.toList());
    }
}
