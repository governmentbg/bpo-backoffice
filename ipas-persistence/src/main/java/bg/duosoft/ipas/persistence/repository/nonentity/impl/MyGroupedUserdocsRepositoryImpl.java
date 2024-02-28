package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.UserdocGroup;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpObject;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.MyGroupedUserdocsRepository;
import bg.duosoft.ipas.util.filter.MyUserdocsFilter;
import bg.duosoft.ipas.util.filter.sorter.MyUserdocsSorterUtils;
import bg.duosoft.ipas.util.home_page.HomePageUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.apache.commons.collections.functors.IfClosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MyGroupedUserdocsRepositoryImpl extends BaseRepositoryCustomImpl implements MyGroupedUserdocsRepository {

    @Autowired
    private UserService userService;

    @Override
    public List<UserdocSimpleResult> getMyUserdocsList(MyUserdocsFilter filter) {
        String buildQuery = buildQuery(filter, false,false);
        if (Objects.isNull(buildQuery)){
            return  new ArrayList<>();
        }
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillResult(query);
    }

    @Override
    public Integer getMyUserdocsCount(MyUserdocsFilter filter) {
        return getCount(filter,true,false);
    }

    @Override
    public Integer getNewlyAllocatedUserdocsCount(MyUserdocsFilter filter) {
        return getCount(filter,true,true);
    }

    private Integer getCount(MyUserdocsFilter filter, boolean isCount, boolean isNewlyAllocatedCount){
        String buildQuery = buildQuery(filter, isCount,isNewlyAllocatedCount);
        if (Objects.isNull(buildQuery)){
            return  0;
        }
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }



    private void constructUserdocGroupNameCondition(MyUserdocsFilter filter, StringBuilder queryBuilder) {
        if (Objects.nonNull(filter.getUserdocGroupName()) && !filter.getUserdocGroupName().isEmpty() && !filter.getUserdocGroupName().equals(UserdocGroup.CORRESP.code())) {
            queryBuilder.append(" AND udt.USERDOC_GROUP_NAME = :userdocGroupName ");
        }
        if (Objects.nonNull(filter.getUserdocGroupName()) && !filter.getUserdocGroupName().isEmpty() && filter.getUserdocGroupName().equals(UserdocGroup.CORRESP.code())) {
            queryBuilder.append(" AND udt.USERDOC_GROUP_NAME like :userdocGroupName ");
        }
    }


    private void constructUserdocGroupNameParam(MyUserdocsFilter filter, Query query) {
        if (Objects.nonNull(filter.getUserdocGroupName()) && !filter.getUserdocGroupName().isEmpty() && !filter.getUserdocGroupName().equals(UserdocGroup.CORRESP.code())) {
            if (filter.getUserdocGroupName().equals(UserdocGroup.CORRESP_EXACT.code())){
                query.setParameter("userdocGroupName", UserdocGroup.CORRESP.code());
            }else{
                query.setParameter("userdocGroupName", filter.getUserdocGroupName());
            }
        }
        if (Objects.nonNull(filter.getUserdocGroupName()) && !filter.getUserdocGroupName().isEmpty() && filter.getUserdocGroupName().equals(UserdocGroup.CORRESP.code())) {
            query.setParameter("userdocGroupName", filter.getUserdocGroupName() + "%");
        }
    }


    @Override
    public Map<String, String> getGroupedUserdocStatuses(MyUserdocsFilter filter) {
        StringBuilder queryBuilder = new StringBuilder(" select p.STATUS_CODE as code,s.status_name as name ");
        queryBuilder.append(" FROM IPASPROD.IP_PROC p ");
        queryBuilder.append(" JOIN IPASPROD.CF_PROCESS_TYPE pt ON p.PROC_TYP = pt.PROC_TYP ");
        queryBuilder.append(" JOIN IPASPROD.CF_USERDOC_TYPE udt ON p.USERDOC_TYP = udt.USERDOC_TYP  ");
        queryBuilder.append(" JOIN IPASPROD.CF_STATUS s ON p.PROC_TYP = s.PROC_TYP AND p.STATUS_CODE = s.STATUS_CODE ");
        queryBuilder.append(" WHERE pt.RELATED_TO_WCODE = 2 ").append(" and (p.USERDOC_FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') or p.USERDOC_FILE_TYP is null) ");
        queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");

        constructUserdocGroupNameCondition(filter,queryBuilder);
        queryBuilder.append(" order by name ");
        Query query = em.createNativeQuery(queryBuilder.toString());


        query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId()));

        constructUserdocGroupNameParam(filter,query);

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

    @Override
    public Map<String, String> getGroupedUserdocObjectType(MyUserdocsFilter filter) {
        StringBuilder queryBuilder = new StringBuilder(" select ft.FILE_TYP as type, ft.FILE_TYPE_NAME as name ");
        queryBuilder.append(" FROM IPASPROD.IP_PROC p ");
        queryBuilder.append(" JOIN IPASPROD.CF_PROCESS_TYPE pt ON p.PROC_TYP = pt.PROC_TYP ");
        queryBuilder.append(" JOIN IPASPROD.CF_USERDOC_TYPE udt ON p.USERDOC_TYP = udt.USERDOC_TYP  ");
        queryBuilder.append(" JOIN IPASPROD.CF_FILE_TYPE ft ON ft.FILE_TYP = p.USERDOC_FILE_TYP ");
        queryBuilder.append(" WHERE pt.RELATED_TO_WCODE = 2 ").append(" and (p.USERDOC_FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') or p.USERDOC_FILE_TYP is null) ");
        queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");

        constructUserdocGroupNameCondition(filter,queryBuilder);

        queryBuilder.append(" group by ft.FILE_TYP,ft.FILE_TYPE_NAME order by ft.FILE_TYPE_NAME ");

        Query query = em.createNativeQuery(queryBuilder.toString());

        query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(SecurityUtils.getLoggedUserId()));

       constructUserdocGroupNameParam(filter,query);

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String,String> fileTypes = new LinkedHashMap<>();
        for (Object[] objects: resultList) {
            String type =(String) objects[0];
            String name =(String) objects[1];
            fileTypes.put(type,name);
        }
        return fileTypes;
    }

    private String buildQuery(MyUserdocsFilter filter, boolean isCount,boolean isNewlyAllocatedCount) {

        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) " : "p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR, d.EXTERNAL_SYSTEM_ID, s.STATUS_CODE, s.STATUS_NAME, udt.USERDOC_TYP, udt.USERDOC_NAME, p.USERDOC_FILE_SEQ,p.USERDOC_FILE_TYP,p.USERDOC_FILE_SER,p.USERDOC_FILE_NBR,(case when f.REGISTRATION_NBR is not null AND f.FILE_TYP!='T' then concat(f.REGISTRATION_NBR,f.REGISTRATION_DUP)\n" +
                "    else convert(varchar(10),f.REGISTRATION_NBR) end) as REGISTRATION_NBR,f.TITLE, d.FILING_DATE,us.USER_NAME as userName, CASE WHEN ch.STATUS = 0 THEN '0' ELSE null END AS newlyAllocated, s2.STATUS_NAME as objectStatusName  ");
        queryBuilder.append(" FROM IPASPROD.IP_PROC p ");
        queryBuilder.append(" JOIN IPASPROD.CF_PROCESS_TYPE pt ON p.PROC_TYP = pt.PROC_TYP ");
        queryBuilder.append(" JOIN IPASPROD.ip_doc d ON p.DOC_ORI = d.doc_ori AND p.DOC_LOG = d.doc_log AND p.doc_ser = d.doc_ser AND p.doc_nbr = d.DOC_NBR ");
        queryBuilder.append(" JOIN IPASPROD.CF_USERDOC_TYPE udt ON p.USERDOC_TYP = udt.USERDOC_TYP ");
        queryBuilder.append(" LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = p.RESPONSIBLE_USER_ID ");
        queryBuilder.append(" LEFT JOIN EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ch on ch.PROC_NBR = p.PROC_NBR and ch.PROC_TYP = p.PROC_TYP ");
        queryBuilder.append(" JOIN IPASPROD.CF_STATUS s ON p.PROC_TYP = s.PROC_TYP AND p.STATUS_CODE = s.STATUS_CODE ");
        queryBuilder.append(" JOIN IPASPROD.IP_PROC p2 on p2.PROC_NBR = p.FILE_PROC_NBR and p2.PROC_TYP = p.FILE_PROC_TYP");
        queryBuilder.append(" JOIN IPASPROD.CF_STATUS s2 ON p2.PROC_TYP = s2.PROC_TYP AND p2.STATUS_CODE = s2.STATUS_CODE");
        queryBuilder.append(" JOIN IPASPROD.IP_FILE f on f.FILE_NBR=p.USERDOC_FILE_NBR and f.FILE_SER = p.USERDOC_FILE_SER and f.FILE_TYP = p.USERDOC_FILE_TYP and f.FILE_SEQ = p.USERDOC_FILE_SEQ ");
        queryBuilder.append(" WHERE pt.RELATED_TO_WCODE = 2 ").append(" and (f.FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') or f.FILE_TYP is null) ");

        if ((Objects.isNull(filter.getInProduction()) || !filter.getInProduction()) &&
                (Objects.isNull(filter.getFinished()) || !filter.getFinished())){
            queryBuilder.append(" AND 1!=1 ");
        }

        queryBuilder.append(" AND p.RESPONSIBLE_USER_ID in (:responsibleUsers) ");
        queryBuilder.append(" and ((ch.STATUS = 0 and ch.NEW_RESPONSIBLE_USER_ID  =  p.RESPONSIBLE_USER_ID )");
        if (!isNewlyAllocatedCount){
            queryBuilder.append(" or ((ch.PROC_NBR is null)" +
                    " or (ch.DATE_CHANGED = ( select MAX(ch2.DATE_CHANGED) from ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES ch2" +
                    " where ch2.PROC_NBR=ch.PROC_NBR and ch2.PROC_TYP=ch.PROC_TYP )) ) ");
        }
        queryBuilder.append(") ");

        if ((Objects.nonNull(filter.getInProduction()) && filter.getInProduction())
          && (Objects.isNull(filter.getFinished()) || !filter.getFinished())){
            queryBuilder.append(" AND s.PROCESS_RESULT_TYP = 'P' ");
        }
        if ((Objects.isNull(filter.getInProduction()) || !filter.getInProduction())
                && (Objects.nonNull(filter.getFinished()) && filter.getFinished())){
            queryBuilder.append(" AND s.PROCESS_RESULT_TYP != 'P' ");
        }

        constructUserdocGroupNameCondition(filter,queryBuilder);
        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()){
            queryBuilder.append(" AND (d.EXTERNAL_SYSTEM_ID like :userdocFilingNumber or " +
                    " CONCAT(p.DOC_ORI, p.DOC_LOG,p.DOC_SER,p.DOC_NBR) like :userdocFilingNumber)  ");
        }

        if(Objects.nonNull(filter.getUserdocFilingDateFrom())){
            queryBuilder.append(" AND d.FILING_DATE >= :userdocFilingDateFrom ");
        }

        if(Objects.nonNull(filter.getUserdocFilingDateTo())){
            queryBuilder.append(" AND d.FILING_DATE <= :userdocFilingDateTo ");
        }

        if(Objects.nonNull(filter.getObjectFileTyp()) && !filter.getObjectFileTyp().isEmpty()){
            queryBuilder.append(" AND p.USERDOC_FILE_TYP = :objectFileTyp ");
        }

        if (Objects.nonNull(filter.getObjectFileNbr()) && !filter.getObjectFileNbr().isEmpty()){
            queryBuilder.append(" AND p.USERDOC_FILE_NBR like :objectFileNbr ");
        }

        if (!StringUtils.isEmpty(filter.getStatusCode())) {
            queryBuilder.append(" AND p.STATUS_CODE = :statusCode ");
        }
        if (!StringUtils.isEmpty(filter.getUserdocType())) {
            queryBuilder.append(" AND p.USERDOC_TYP = :userdocType ");
        }

        if (filter.getNewlyAllocated()){
            queryBuilder.append(" AND ch.STATUS = 0 ");
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            if (Objects.nonNull(sortColumn)){
                String[] columns = MyUserdocsSorterUtils.sorterColumnMap().get(sortColumn).split(",");
                String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
                queryBuilder.append(" ORDER BY ").append(order);
            }
        }
        return queryBuilder.toString();
    }

    private void addQueryParams(MyUserdocsFilter filter, Query query) {
        query.setParameter("responsibleUsers", HomePageUtils.getResponsibleUsersForMyObjectPanels(userService,filter.getResponsibleUser()));
        constructUserdocGroupNameParam(filter,query);

        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()){
            query.setParameter("userdocFilingNumber", "%"+ filter.getUserdocFilingNumber()+"%");
        }

        if(Objects.nonNull(filter.getUserdocFilingDateFrom())){
            query.setParameter("userdocFilingDateFrom", filter.getUserdocFilingDateFrom());
        }

        if(Objects.nonNull(filter.getUserdocFilingDateTo())){
            query.setParameter("userdocFilingDateTo", filter.getUserdocFilingDateTo());
        }

        if (Objects.nonNull(filter.getObjectFileNbr()) && !filter.getObjectFileNbr().isEmpty()){
            query.setParameter("objectFileNbr", "%"+ filter.getObjectFileNbr()+"%");
        }

        if(Objects.nonNull(filter.getObjectFileTyp()) && !filter.getObjectFileTyp().isEmpty()){
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
                .map(object ->
                        new UserdocSimpleResult(
                        new CDocumentId((String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue()),
                                (Date) object[15],null , null, null, (String) object[4],
                        (String) object[5], (String) object[6], (String) object[7], (String) object[8],new UserdocIpObject((object[13]==null?null:object[13].toString()),(String) object[14],
                                new CFileId((String) object[9],(String) object[10],((BigDecimal) object[11]).intValue(),((BigDecimal) object[12]).intValue()),null,(String) object[18]),(String) object[16],null,null,null,(String) object[17]
                ))
                .collect(Collectors.toList());
    }
}
