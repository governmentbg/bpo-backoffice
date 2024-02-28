package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.structure.User;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpObject;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.ReceptionUserdocRequestRepositoryCustom;
import bg.duosoft.ipas.util.filter.ReceptionUserdocListFilter;
import bg.duosoft.ipas.util.filter.sorter.ReceptionUserdocSorterUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReceptionUserdocRequestRepositoryCustomImpl extends BaseRepositoryCustomImpl implements ReceptionUserdocRequestRepositoryCustom {


    @Autowired
    private UserService userService;


    private String getDepartmentRestrictionCriteria() {
        User loggedUser = userService.getUser(SecurityUtils.getLoggedUserId());
        String sql;
        if (StringUtils.isEmpty(loggedUser.getOfficeDivisionCode()) || StringUtils.isEmpty(loggedUser.getOfficeDepartmentCode())) {
            sql = " and 1!=1 ";
        } else {
            sql = " and udocd.OFFICE_DIVISION_CODE = " + loggedUser.getOfficeDivisionCode() + " and udocd.OFFICE_DEPARTMENT_CODE =" + loggedUser.getOfficeDepartmentCode() + "\n";
        }
        return sql;
    }

    @Override
    public List<UserdocSimpleResult> selectUserdocReceptions(ReceptionUserdocListFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillResult(query);
    }


    @Override
    public int selectUserdocReceptionsCount(ReceptionUserdocListFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }


    @Override
    public Map<String, String> getUserdocObjectTypes(ReceptionUserdocListFilter filter) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select p.USERDOC_FILE_TYP as type, ft.FILE_TYPE_NAME as name from EXT_RECEPTION.RECEPTION_USERDOC_REQUEST r\n" +
                "JOIN IPASPROD.IP_PROC p on r.DOC_ORI = p.DOC_ORI AND r.DOC_LOG = p.DOC_LOG AND r.DOC_SER = p.DOC_SER AND r.DOC_NBR = p.DOC_NBR\n" +
                "JOIN IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
                "JOIN IPASPROD.CF_PROCESS_TYPE prt on  (s.STATUS_CODE = prt.PRIMARY_INI_STATUS_CODE or s.STATUS_CODE = prt.SECONDARY_INI_STATUS_CODE ) and prt.PROC_TYP = p.PROC_TYP\n" +
                "JOIN IPASPROD.CF_FILE_TYPE ft on ft.FILE_TYP = p.USERDOC_FILE_TYP\n" +
                "JOIN IPASPROD.CF_USERDOC_TYPE udt on udt.USERDOC_TYP = p.USERDOC_TYP\n" +
                "join ext_core.CF_USERDOC_TYPE_DEPARTMENT udocd on udocd.USERDOC_TYP = p.USERDOC_TYP\n" +
                "where p.RESPONSIBLE_USER_ID IS NULL ")
                .append(getDepartmentRestrictionCriteria())
                .append(" and (p.USERDOC_FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') or p.USERDOC_FILE_TYP is null) ");
        queryBuilder.append(" and udt.USERDOC_GROUP_NAME in (:userdocTypeGroups) ");
        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()) {
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }
        queryBuilder.append(" group by p.USERDOC_FILE_TYP,ft.FILE_TYPE_NAME order by ft.FILE_TYPE_NAME ");

        Query query = em.createNativeQuery(queryBuilder.toString());
        query.setParameter("userdocTypeGroups", filter.getUserdocTypeGroups());

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

    @Override
    public Map<String, String> getUserdocStatuses(ReceptionUserdocListFilter filter) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select s.STATUS_CODE as code, s.STATUS_NAME as name from EXT_RECEPTION.RECEPTION_USERDOC_REQUEST r\n" +
                "JOIN IPASPROD.IP_PROC p on r.DOC_ORI = p.DOC_ORI AND r.DOC_LOG = p.DOC_LOG AND r.DOC_SER = p.DOC_SER AND r.DOC_NBR = p.DOC_NBR\n" +
                "JOIN IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
                "JOIN IPASPROD.CF_PROCESS_TYPE prt on (s.STATUS_CODE = prt.PRIMARY_INI_STATUS_CODE or s.STATUS_CODE = prt.SECONDARY_INI_STATUS_CODE ) and prt.PROC_TYP = p.PROC_TYP\n" +
                "join ext_core.CF_USERDOC_TYPE_DEPARTMENT udocd on udocd.USERDOC_TYP = p.USERDOC_TYP\n" +
                "where p.RESPONSIBLE_USER_ID IS NULL ").append(getDepartmentRestrictionCriteria())
                .append(" and (p.USERDOC_FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') or p.USERDOC_FILE_TYP is null) ")
                .append(" group by s.STATUS_CODE , s.STATUS_NAME order by s.STATUS_NAME ");
        Query query = em.createNativeQuery(queryBuilder.toString());

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

    @Override
    public Map<String, String> getUserdocTypes(ReceptionUserdocListFilter filter) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select udt.USERDOC_TYP as type, udt.USERDOC_NAME  as name from EXT_RECEPTION.RECEPTION_USERDOC_REQUEST r\n" +
                "JOIN IPASPROD.IP_PROC p on r.DOC_ORI = p.DOC_ORI AND r.DOC_LOG = p.DOC_LOG AND r.DOC_SER = p.DOC_SER AND r.DOC_NBR = p.DOC_NBR\n" +
                "JOIN IPASPROD.CF_USERDOC_TYPE udt on udt.USERDOC_TYP = p.USERDOC_TYP\n" +
                "JOIN IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE\n" +
                "JOIN IPASPROD.CF_PROCESS_TYPE prt on  (s.STATUS_CODE = prt.PRIMARY_INI_STATUS_CODE or s.STATUS_CODE = prt.SECONDARY_INI_STATUS_CODE ) and prt.PROC_TYP = p.PROC_TYP\n" +
                "join ext_core.CF_USERDOC_TYPE_DEPARTMENT udocd on udocd.USERDOC_TYP = p.USERDOC_TYP\n" +
                "where p.RESPONSIBLE_USER_ID IS NULL ").append(getDepartmentRestrictionCriteria())
                .append(" and (p.USERDOC_FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') or p.USERDOC_FILE_TYP is null) ");
        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()) {
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }
        queryBuilder.append(" and udt.USERDOC_GROUP_NAME in (:userdocTypeGroups) ");
        queryBuilder.append(" group by udt.USERDOC_TYP , udt.USERDOC_NAME order by udt.USERDOC_NAME ");

        Query query = em.createNativeQuery(queryBuilder.toString());

        query.setParameter("userdocTypeGroups", filter.getUserdocTypeGroups());

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String, String> userdocTypes = new LinkedHashMap<>();
        for (Object[] objects : resultList) {
            String type = (String) objects[0];
            String name = (String) objects[1];
            userdocTypes.put(type, name);
        }
        return userdocTypes;
    }

    private String buildQuery(ReceptionUserdocListFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " r.DOC_ORI, r.DOC_LOG, r.DOC_SER, r.DOC_NBR, " +
                        "       r.FILING_DATE, st.NAME, r.ORIGINAL_EXPECTED, r.NOTES, d.EXTERNAL_SYSTEM_ID, " +
                        "       s.STATUS_CODE, s.STATUS_NAME, p.USERDOC_TYP, ut.USERDOC_NAME, p.USERDOC_FILE_SEQ,p.USERDOC_FILE_TYP,p.USERDOC_FILE_SER,p.USERDOC_FILE_NBR,(case when f.REGISTRATION_NBR is not null AND f.FILE_TYP!='T' then concat(f.REGISTRATION_NBR,f.REGISTRATION_DUP)\n" +
                        "    else convert(varchar(10),f.REGISTRATION_NBR) end) as REGISTRATION_NBR,f.TITLE, r.CREATE_DATE, p.PROC_TYP, p.PROC_NBR, us.USER_NAME ")
                .append(" FROM EXT_RECEPTION.RECEPTION_USERDOC_REQUEST r ")
                .append(" JOIN EXT_RECEPTION.CF_SUBMISSION_TYPE st on r.SUBMISSION_TYPE = st.ID ")
                .append(" JOIN IPASPROD.IP_DOC d on r.DOC_ORI = d.DOC_ORI AND r.DOC_LOG = d.DOC_LOG AND r.DOC_SER = d.DOC_SER AND r.DOC_NBR = d.DOC_NBR ")
                .append(" JOIN IPASPROD.IP_PROC p on r.DOC_ORI = p.DOC_ORI AND r.DOC_LOG = p.DOC_LOG AND r.DOC_SER = p.DOC_SER AND r.DOC_NBR = p.DOC_NBR ")
                .append(" JOIN IPASPROD.CF_USERDOC_TYPE ut on ut.USERDOC_TYP = p.USERDOC_TYP ")
                .append(" JOIN IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE ")
                .append(" JOIN IPASPROD.CF_PROCESS_TYPE prt on  (s.STATUS_CODE = prt.PRIMARY_INI_STATUS_CODE or s.STATUS_CODE = prt.SECONDARY_INI_STATUS_CODE ) and prt.PROC_TYP = p.PROC_TYP ")
                .append(" JOIN IPASPROD.IP_FILE f on f.FILE_NBR=p.USERDOC_FILE_NBR and f.FILE_SER = p.USERDOC_FILE_SER and f.FILE_TYP = p.USERDOC_FILE_TYP and f.FILE_SEQ = p.USERDOC_FILE_SEQ ")
                .append(" join IP_PROC pr2 on pr2.PROC_NBR = f.PROC_NBR and pr2.PROC_TYP = f.PROC_TYP ")
                .append(" join ext_core.CF_USERDOC_TYPE_DEPARTMENT udocd on udocd.USERDOC_TYP = p.USERDOC_TYP ")
                .append(" left join IP_USER us on us.USER_ID = pr2.RESPONSIBLE_USER_ID ")
                .append(" WHERE 1=1 ")
                .append(getDepartmentRestrictionCriteria())
                .append(" AND p.RESPONSIBLE_USER_ID IS NULL ").append(" and f.FILE_TYP not in ('" + FileType.SINGLE_DESIGN.code() + "'" + ",'" + FileType.INTERNATIONAL_SINGLE_DESIGN.code() + "') ");


        queryBuilder.append(" AND ut.USERDOC_GROUP_NAME in (:userdocTypeGroups) ");

        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()) {
            queryBuilder.append(" AND (d.EXTERNAL_SYSTEM_ID like :userdocFilingNumber or " +
                    " CONCAT(r.DOC_ORI, r.DOC_LOG,r.DOC_SER,r.DOC_NBR) like :userdocFilingNumber)  ");
        }

        if (Objects.nonNull(filter.getUserdocFilingDateFrom())) {
            queryBuilder.append(" AND r.FILING_DATE >= :userdocFilingDateFrom ");
        }
        if (Objects.nonNull(filter.getUserdocFilingDateTo())) {
            queryBuilder.append(" AND r.FILING_DATE <= :userdocFilingDateTo ");
        }

        if (Objects.nonNull(filter.getObjectFileTyp()) && !filter.getObjectFileTyp().isEmpty()) {
            queryBuilder.append(" AND p.USERDOC_FILE_TYP = :objectFileTyp ");
        }

        if (Objects.nonNull(filter.getObjectFileNbr()) && !filter.getObjectFileNbr().isEmpty()) {
            queryBuilder.append(" AND p.USERDOC_FILE_NBR like :objectFileNbr ");
        }

        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            queryBuilder.append(" AND s.STATUS_CODE = :statusCode ");
        }

        if (!StringUtils.isEmpty(filter.getUserdocType())) {
            queryBuilder.append(" AND p.USERDOC_TYP = :userdocType ");
        }


        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()) {
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = ReceptionUserdocSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        return queryBuilder.toString();
    }

    private void addQueryParams(ReceptionUserdocListFilter filter, Query query) {
        query.setParameter("userdocTypeGroups", filter.getUserdocTypeGroups());

        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()) {
            query.setParameter("userdocFilingNumber", "%" + filter.getUserdocFilingNumber() + "%");
        }

        if (Objects.nonNull(filter.getUserdocFilingDateFrom())) {
            query.setParameter("userdocFilingDateFrom", filter.getUserdocFilingDateFrom());
        }
        if (Objects.nonNull(filter.getUserdocFilingDateTo())) {
            query.setParameter("userdocFilingDateTo", filter.getUserdocFilingDateTo());
        }

        if (Objects.nonNull(filter.getObjectFileNbr()) && !filter.getObjectFileNbr().isEmpty()) {
            query.setParameter("objectFileNbr", "%" + filter.getObjectFileNbr() + "%");
        }

        if (Objects.nonNull(filter.getObjectFileTyp()) && !filter.getObjectFileTyp().isEmpty()) {
            query.setParameter("objectFileTyp", filter.getObjectFileTyp());
        }

        String statusCode = filter.getStatusCode();
        if (Objects.nonNull(statusCode) && !statusCode.isEmpty()) {
            query.setParameter("statusCode", statusCode);
        }
        String userdocType = filter.getUserdocType();
        if (!StringUtils.isEmpty(userdocType)) {
            query.setParameter("userdocType", userdocType);
        }

    }

    private List<UserdocSimpleResult> fillResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        return resultList.stream()
                .map(object -> new UserdocSimpleResult(
                        new CDocumentId((String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue()),
                        (Date) object[4], (String) object[5], (Boolean) object[6], (String) object[7], (String) object[8],
                        (String) object[9], (String) object[10], (String) object[11], (String) object[12], new UserdocIpObject((object[17] == null ? null : object[17].toString()), (String) object[18],
                        new CFileId((String) object[13], (String) object[14], ((BigDecimal) object[15]).intValue(), ((BigDecimal) object[16]).intValue()), (object[22] == null ? null : object[22].toString()), null), null, null, (Date) object[19], ((String) object[20]).concat("-").concat(((BigDecimal) object[21]).toString()), null
                ))
                .collect(Collectors.toList());
    }


    private String constructConditionsRelatedToRoles() {
        StringBuilder queryBuilder = new StringBuilder();
        if (!SecurityUtils.hasRights(SecurityRole.MarkViewOwn)) {
            queryBuilder.append(" AND p.USERDOC_FILE_TYP not in (" + BasicUtils.getMarkRelatedFileTypesAsSequence() + ") ");
        }
        if (!SecurityUtils.hasRights(SecurityRole.PatentViewOwn)) {
            queryBuilder.append(" AND p.USERDOC_FILE_TYP not in (" + BasicUtils.getPatentRelatedFileTypesAsSequence() + ") ");
        }
        return queryBuilder.toString();
    }

}
