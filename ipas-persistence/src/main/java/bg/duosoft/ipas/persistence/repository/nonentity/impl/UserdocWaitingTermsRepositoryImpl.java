package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpObject;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.UserdocWaitingTermsRepository;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import bg.duosoft.ipas.util.filter.sorter.MyUserdocsSorterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserdocWaitingTermsRepositoryImpl extends BaseRepositoryCustomImpl implements UserdocWaitingTermsRepository {

    @Autowired
    private UserService userService;

    @Override
    public List<UserdocSimpleResult> getUserdocWaitingTermsList(MyUserdocsFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillResult(query);
    }

    @Override
    public Integer getUserdocWaitingTermsCount(MyUserdocsFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public Map<String, String> getUserdocWaitingTermsUserdocTypesMap(MyUserdocsFilter filter) {
        StringBuilder queryBuilder =new StringBuilder();
        queryBuilder.append("SELECT udt.USERDOC_TYP, udt.USERDOC_NAME\n" +
                "FROM IPASPROD.IP_PROC p\n" +
                "JOIN IPASPROD.CF_USERDOC_TYPE udt ON p.USERDOC_TYP = udt.USERDOC_TYP  JOIN IPASPROD.CF_STATUS s ON p.PROC_TYP = s.PROC_TYP AND p.STATUS_CODE = s.STATUS_CODE\n" +
                "WHERE p.EXPIRATION_DATE is not null and p.USERDOC_TYP is not null  and p.USERDOC_TYP not in('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') ");

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
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

        Map<String,String> userdocTypeMap = new LinkedHashMap<>();
        for (Object[] objects: resultList) {
            String userdocTyp =(String) objects[0];
            String userdocTypName =(String) objects[1];
            if (!userdocTypeMap.containsKey(userdocTyp)){
                userdocTypeMap.put(userdocTyp,userdocTypName);
            }
        }
        return userdocTypeMap;
    }

    private String buildQuery(MyUserdocsFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) " : "p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR, d.EXTERNAL_SYSTEM_ID, s.STATUS_CODE, s.STATUS_NAME, udt.USERDOC_TYP, udt.USERDOC_NAME, p.USERDOC_FILE_SEQ,p.USERDOC_FILE_TYP,p.USERDOC_FILE_SER,p.USERDOC_FILE_NBR,(case when f.REGISTRATION_NBR is not null AND f.FILE_TYP!='T' then concat(f.REGISTRATION_NBR,f.REGISTRATION_DUP)\n" +
                "    else convert(varchar(10),f.REGISTRATION_NBR) end) as REGISTRATION_NBR,f.TITLE,us.USER_NAME as userName,cast(p.EXPIRATION_DATE as date)  as expirationDate ");
        queryBuilder.append(" FROM IPASPROD.IP_PROC p ");
        queryBuilder.append(" JOIN IPASPROD.CF_PROCESS_TYPE pt ON p.PROC_TYP = pt.PROC_TYP ");
        queryBuilder.append(" JOIN IPASPROD.ip_doc d ON p.DOC_ORI = d.doc_ori AND p.DOC_LOG = d.doc_log AND p.doc_ser = d.doc_ser AND p.doc_nbr = d.DOC_NBR ");
        queryBuilder.append(" JOIN IPASPROD.CF_USERDOC_TYPE udt ON p.USERDOC_TYP = udt.USERDOC_TYP ");
        queryBuilder.append(" JOIN IPASPROD.CF_STATUS s ON p.PROC_TYP = s.PROC_TYP AND p.STATUS_CODE = s.STATUS_CODE ");
        queryBuilder.append(" LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID ");
        queryBuilder.append(" JOIN IPASPROD.IP_FILE f on f.FILE_NBR=p.USERDOC_FILE_NBR and f.FILE_SER = p.USERDOC_FILE_SER and f.FILE_TYP = p.USERDOC_FILE_TYP and f.FILE_SEQ = p.USERDOC_FILE_SEQ ");
        queryBuilder.append(" WHERE p.EXPIRATION_DATE is not null and p.USERDOC_TYP is not null ").append(" and (f.FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') or f.FILE_TYP is null) ");

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }
        if (!StringUtils.isEmpty(filter.getUserdocType())) {
            queryBuilder.append(" AND p.USERDOC_TYP = :userdocType ");
        }
        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            if(Objects.nonNull(sortColumn)){
                String[] columns = MyUserdocsSorterUtils.sorterColumnMap().get(sortColumn).split(",");
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
                .map(object ->
                        new UserdocSimpleResult(
                                new CDocumentId((String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue()),
                                null,null , null, null, (String) object[4],
                                (String) object[5], (String) object[6], (String) object[7], (String) object[8],new UserdocIpObject((object[13]==null?null:object[13].toString()),(String) object[14],
                                new CFileId((String) object[9],(String) object[10],((BigDecimal) object[11]).intValue(),((BigDecimal) object[12]).intValue()),null,null),(String) object[15],(Date) object[16], null,null,null
                        ))
                .collect(Collectors.toList());
    }
}
