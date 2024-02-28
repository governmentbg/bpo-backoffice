package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpObject;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.UserdocCorrespondenceTermsRepository;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import bg.duosoft.ipas.util.filter.sorter.UserdocCorrespondenceTermsSortedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserdocCorrespondenceTermsRepositoryImpl extends BaseRepositoryCustomImpl implements UserdocCorrespondenceTermsRepository {

    @Autowired
    private UserService userService;

    private final int USERDOC_CORRESPONDENCE_TERMS_PANEL_MAX_COUNT = 500;

    @Override
    public List<UserdocSimpleResult> getUserdocCorrespondenceTermsList(MyUserdocsFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillResult(query);
    }

    @Override
    public Integer getUserdocCorrespondenceTermsCount(MyUserdocsFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();

        return result.intValue() > USERDOC_CORRESPONDENCE_TERMS_PANEL_MAX_COUNT ? USERDOC_CORRESPONDENCE_TERMS_PANEL_MAX_COUNT : result.intValue();
    }

    @Override
    public Map<String, String> getUserdocCorrespondenceTermsUserdocTypesMap(MyUserdocsFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select udt.USERDOC_TYP,udt.USERDOC_NAME\n" +
                "from ipasprod.ip_action a\n" +
                "join ipasprod.CF_ACTION_TYPE at on a.ACTION_TYP = at.ACTION_TYP\n" +
                "join ipasprod.ip_proc p on a.PROC_NBR = p.PROC_NBR and a.PROC_TYP = p.PROC_TYP\n" +
                "join ipasprod.CF_USERDOC_TYPE udt on p.USERDOC_TYP=udt.USERDOC_TYP\n" +
                "where at.AUTOMATIC_ACTION_WCODE=1  and p.doc_nbr is not null and p.EXPIRATION_DATE>dateadd(month, -6, current_timestamp)\n" +
                "union all\n" +
                "select  udt.USERDOC_TYP,udt.USERDOC_NAME\n" +
                "from ipasprod.ip_proc p\n" +
                "join ipasprod.CF_USERDOC_TYPE udt on p.USERDOC_TYP=udt.USERDOC_TYP\n" +
                "where p.EXPIRATION_DATE is not null and p.doc_nbr is not null ");

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }
        queryBuilder.append("order by USERDOC_NAME");
        Query query = em.createNativeQuery(queryBuilder.toString());

        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String, String> userdocTypeMap = new LinkedHashMap<>();
        for (Object[] objects : resultList) {
            String userdocTyp = (String) objects[0];
            String userdocTypName = (String) objects[1];
            if (!userdocTypeMap.containsKey(userdocTyp)) {
                userdocTypeMap.put(userdocTyp, userdocTypName);
            }
        }
        return userdocTypeMap;
    }

    @Override
    public Map<String, String> getStatuses(MyUserdocsFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cs.STATUS_CODE as code, cs.STATUS_NAME as name\n" +
                "from ipasprod.ip_action a\n" +
                "join ipasprod.CF_ACTION_TYPE at on a.ACTION_TYP = at.ACTION_TYP\n" +
                "join ipasprod.ip_proc p on a.PROC_NBR = p.PROC_NBR and a.PROC_TYP = p.PROC_TYP\n" +
                "join ipasprod.CF_STATUS cs on cs.STATUS_CODE  = p.STATUS_CODE and cs.PROC_TYP = p.PROC_TYP \n" +
                "where at.AUTOMATIC_ACTION_WCODE=1  and p.doc_nbr is not null and p.EXPIRATION_DATE>dateadd(month, -6, current_timestamp) ");
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }
        queryBuilder.append(" group by cs.STATUS_CODE , cs.STATUS_NAME\n" +
                "union all ");
        queryBuilder.append(" select cs.STATUS_CODE as code, cs.STATUS_NAME as name\n" +
                "from ipasprod.ip_proc p\n" +
                "join ipasprod.CF_STATUS cs on cs.STATUS_CODE  = p.STATUS_CODE and cs.PROC_TYP = p.PROC_TYP \n" +
                "where p.EXPIRATION_DATE is not null and p.doc_nbr is not null ");
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }
        queryBuilder.append("group by cs.STATUS_CODE , cs.STATUS_NAME ");

        Query query = em.createNativeQuery(queryBuilder.toString());
        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String, String> statuses = new LinkedHashMap<>();
        for (Object[] objects : resultList) {
            String code = (String) objects[0];
            String name = (String) objects[1];
            statuses.put(code, name);
        }
        return statuses;
    }

    @Override
    public Map<String, String> getUserdocCorrespondenceTermsObjectType(MyUserdocsFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cf.FILE_TYP as type, cf.FILE_TYPE_NAME as name\n" +
                "from ipasprod.ip_action a\n" +
                "join ipasprod.CF_ACTION_TYPE at on a.ACTION_TYP = at.ACTION_TYP\n" +
                "join ipasprod.ip_proc p on a.PROC_NBR = p.PROC_NBR and a.PROC_TYP = p.PROC_TYP\n" +
                "JOIN IPASPROD.CF_FILE_TYPE cf on cf.FILE_TYP = p.USERDOC_FILE_TYP\n" +
                "where at.AUTOMATIC_ACTION_WCODE=1  and p.doc_nbr is not null and p.EXPIRATION_DATE>dateadd(month, -6, current_timestamp) ");
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }
        queryBuilder.append(" group by cf.FILE_TYP , cf.FILE_TYPE_NAME\n" +
                "union all ");
        queryBuilder.append(" select cf.FILE_TYP as type, cf.FILE_TYPE_NAME as name\n" +
                "from ipasprod.ip_proc p\n" +
                "JOIN IPASPROD.CF_FILE_TYPE cf on cf.FILE_TYP = p.USERDOC_FILE_TYP\n" +
                "where p.EXPIRATION_DATE is not null and p.doc_nbr is not null ");
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        }
        queryBuilder.append(" group by cf.FILE_TYP , cf.FILE_TYPE_NAME ");

        Query query = em.createNativeQuery(queryBuilder.toString());
        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String, String> fileTypes = new LinkedHashMap<>();
        for (Object[] objects : resultList) {
            String type = (String) objects[0];
            String name = (String) objects[1];
            fileTypes.put(type, name);
        }
        return fileTypes;
    }


    private String buildExpirationDateConditions(MyUserdocsFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();
        if (filter.getInTerm() == false && filter.getFinished() == false) {
            queryBuilder.append(" and expirationDate is null  ");
        }
        if (filter.getInTerm() == true && filter.getFinished() == false) {
            queryBuilder.append(" and expirationDate is not null and expirationDate>getdate()  ");
        }
        if (filter.getInTerm() == false && filter.getFinished() == true) {
            queryBuilder.append(" and (expirationDate is null or expirationDate<getdate()) ");
        }
        if (filter.getInTerm() == true && filter.getFinished() == true) {
            queryBuilder.append(" and ( (expirationDate is not null and expirationDate>getdate()) or ((expirationDate is null or expirationDate<getdate())) ) ");
        }
        return queryBuilder.toString();
    }

    private String buildQueryConditions(MyUserdocsFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }
        if (!StringUtils.isEmpty(filter.getUserdocType())) {
            queryBuilder.append(" AND  p.USERDOC_TYP = :userdocType ");
        }

        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            queryBuilder.append(" AND cs.STATUS_CODE = :statusCode ");
        }

        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()) {
            queryBuilder.append(" AND (d.EXTERNAL_SYSTEM_ID like :userdocFilingNumber or " +
                    " CONCAT(p.DOC_ORI, p.DOC_LOG,p.DOC_SER,p.DOC_NBR) like :userdocFilingNumber)  ");
        }
        if (Objects.nonNull(filter.getObjectFileTyp()) && !filter.getObjectFileTyp().isEmpty()) {
            queryBuilder.append(" AND p.USERDOC_FILE_TYP = :objectFileTyp ");
        }
        if (Objects.nonNull(filter.getObjectFileNbr()) && !filter.getObjectFileNbr().isEmpty()) {
            queryBuilder.append(" AND p.USERDOC_FILE_NBR like :objectFileNbr ");
        }
        return queryBuilder.toString();
    }

    private String buildQuery(MyUserdocsFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) as count " : " * ");
        queryBuilder.append(" from ( select p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR, d.EXTERNAL_SYSTEM_ID, udt.USERDOC_TYP, udt.USERDOC_NAME, p.USERDOC_FILE_SEQ,p.USERDOC_FILE_TYP,p.USERDOC_FILE_SER,p.USERDOC_FILE_NBR,(case when f.REGISTRATION_NBR is not null AND f.FILE_TYP!='T' then concat(f.REGISTRATION_NBR,f.REGISTRATION_DUP)\n" +
                "    else convert(varchar(10),f.REGISTRATION_NBR) end) as REGISTRATION_NBR,f.TITLE,us.USER_NAME as userName, cast(a.ACTION_DATE as date) as dtexp, p.RESPONSIBLE_USER_ID,f.FILE_TYP, cast(a.ACTION_DATE as date)  as expirationDate,cs.STATUS_NAME as statusName \n" +
                "from ipasprod.ip_action a\n" +
                "join ipasprod.CF_ACTION_TYPE at on a.ACTION_TYP = at.ACTION_TYP\n" +
                "join ipasprod.ip_proc p on a.PROC_NBR = p.PROC_NBR and a.PROC_TYP = p.PROC_TYP\n" +
                "join ipasprod.CF_USERDOC_TYPE udt on p.USERDOC_TYP=udt.USERDOC_TYP\n" +
                " INNER JOIN CF_STATUS cs on cs.STATUS_CODE = p.STATUS_CODE and cs.PROC_TYP = p.PROC_TYP " +
                "JOIN IPASPROD.ip_doc d ON p.DOC_ORI = d.doc_ori AND p.DOC_LOG = d.doc_log AND p.doc_ser = d.doc_ser AND p.doc_nbr = d.DOC_NBR\n" +
                "LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID \n" +
                "JOIN IPASPROD.IP_FILE f on f.FILE_NBR=p.USERDOC_FILE_NBR and f.FILE_SER = p.USERDOC_FILE_SER and f.FILE_TYP = p.USERDOC_FILE_TYP and f.FILE_SEQ = p.USERDOC_FILE_SEQ \n" +
                "where at.AUTOMATIC_ACTION_WCODE=1  and p.doc_nbr is not null and a.ACTION_DATE>dateadd(month, -6, current_timestamp) \n");
        queryBuilder.append(buildQueryConditions(filter));
        queryBuilder.append("union\n" +
                "select p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR, d.EXTERNAL_SYSTEM_ID, udt.USERDOC_TYP, udt.USERDOC_NAME, p.USERDOC_FILE_SEQ,p.USERDOC_FILE_TYP,p.USERDOC_FILE_SER,p.USERDOC_FILE_NBR,(case when f.REGISTRATION_NBR is not null AND f.FILE_TYP!='T' then concat(f.REGISTRATION_NBR,f.REGISTRATION_DUP)\n" +
                "    else convert(varchar(10),f.REGISTRATION_NBR) end) as REGISTRATION_NBR,f.TITLE,us.USER_NAME as userName, cast(p.EXPIRATION_DATE as date) as dtexp,p.RESPONSIBLE_USER_ID,f.FILE_TYP, cast(p.EXPIRATION_DATE as date)  as expirationDate,cs.STATUS_NAME as statusName  \n" +
                "from ipasprod.ip_proc p\n" +
                "join ipasprod.CF_USERDOC_TYPE udt on p.USERDOC_TYP=udt.USERDOC_TYP\n" +
                " INNER JOIN CF_STATUS cs on cs.STATUS_CODE = p.STATUS_CODE and cs.PROC_TYP = p.PROC_TYP " +
                "JOIN IPASPROD.ip_doc d ON p.DOC_ORI = d.doc_ori AND p.DOC_LOG = d.doc_log AND p.doc_ser = d.doc_ser AND p.doc_nbr = d.DOC_NBR\n" +
                "LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID \n" +
                "JOIN IPASPROD.IP_FILE f on f.FILE_NBR=p.USERDOC_FILE_NBR and f.FILE_SER = p.USERDOC_FILE_SER and f.FILE_TYP = p.USERDOC_FILE_TYP and f.FILE_SEQ = p.USERDOC_FILE_SEQ \n" +
                "where p.doc_nbr is not null and p.EXPIRATION_DATE is not null \n");
        queryBuilder.append(buildQueryConditions(filter));
        queryBuilder.append(" ) as correspondenceTerms where 1=1").append(" and (FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') or FILE_TYP is null) ");
        queryBuilder.append(buildExpirationDateConditions(filter));

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            if (Objects.nonNull(sortColumn)) {
                String[] columns = UserdocCorrespondenceTermsSortedUtils.sorterColumnMap().get(sortColumn).split(",");
                String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
                queryBuilder.append(" ORDER BY ").append(order);
            }
        }
        return queryBuilder.toString();
    }

    private void addQueryParams(MyUserdocsFilter filter, Query query) {
        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }
        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()) {
            query.setParameter("userdocFilingNumber", "%" + filter.getUserdocFilingNumber() + "%");
        }
        if (Objects.nonNull(filter.getObjectFileNbr()) && !filter.getObjectFileNbr().isEmpty()) {
            query.setParameter("objectFileNbr", "%" + filter.getObjectFileNbr() + "%");
        }
        if (Objects.nonNull(filter.getObjectFileTyp()) && !filter.getObjectFileTyp().isEmpty()) {
            query.setParameter("objectFileTyp", filter.getObjectFileTyp());
        }
        String userdocType = filter.getUserdocType();
        if (!StringUtils.isEmpty(userdocType)) {
            query.setParameter("userdocType", userdocType);
        }

        String statusCode = filter.getStatusCode();
        if (!StringUtils.isEmpty(statusCode)) {
            query.setParameter("statusCode", statusCode);
        }
    }

    private List<UserdocSimpleResult> fillResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        return resultList.stream()
                .map(object ->
                        new UserdocSimpleResult(
                                new CDocumentId((String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue()),
                                null, null, null, null, (String) object[4],
                                null, (String) object[18], (String) object[5], (String) object[6], new UserdocIpObject((object[11] == null ? null : object[11].toString()), (String) object[12],
                                new CFileId((String) object[7], (String) object[8], ((BigDecimal) object[9]).intValue(), ((BigDecimal) object[10]).intValue()), null, null), (String) object[13], (Date) object[14], null, null, null
                        ))
                .collect(Collectors.toList());
    }
}
