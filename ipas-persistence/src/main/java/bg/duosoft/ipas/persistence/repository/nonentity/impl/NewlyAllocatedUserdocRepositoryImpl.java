package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.nonentity.NewlyAllocatedUserdocSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpObject;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.NewlyAllocatedUserdocRepository;
import bg.duosoft.ipas.util.filter.NewlyAllocatedUserdocFilter;
import bg.duosoft.ipas.util.filter.sorter.NewlyAllocatedUserdocSorterUtils;
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
public class NewlyAllocatedUserdocRepositoryImpl extends BaseRepositoryCustomImpl implements NewlyAllocatedUserdocRepository {

    @Autowired
    private UserService userService;

    @Override
    public List<NewlyAllocatedUserdocSimpleResult> selectNewlyAllocatedUserdocs(NewlyAllocatedUserdocFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        return fillResult(query);
    }

    @Override
    public int selectNewlyAllocatedUserdocsCount(NewlyAllocatedUserdocFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public Map<String, String> getUserdocTypes(NewlyAllocatedUserdocFilter filter) {
        StringBuilder queryBuilder = new StringBuilder(" select  ut.USERDOC_TYP as type, ut.USERDOC_NAME from ip_proc pr\n" +
                "inner join ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES puc on puc.PROC_TYP = pr.PROC_TYP and puc.PROC_NBR = pr.PROC_NBR\n" +
                "inner join  IPASPROD.CF_USERDOC_TYPE ut on ut.USERDOC_TYP = pr.USERDOC_TYP ");
        queryBuilder .append(" where (pr.USERDOC_FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"')) ");
        queryBuilder.append(excludedUserdocTypesCondition());
        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()){
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND puc.NEW_RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }
        queryBuilder.append(" group by ut.USERDOC_TYP , ut.USERDOC_NAME order by ut.USERDOC_NAME ");

        Query query = em.createNativeQuery(queryBuilder.toString());

        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Map<String,String> userdocTypes = new LinkedHashMap<>();
        for (Object[] objects: resultList) {
            String type =(String) objects[0];
            String name =(String) objects[1];
            userdocTypes.put(type,name);
        }
        return userdocTypes;
    }

    @Override
    public Map<String, String> getUserdocObjectTypes(NewlyAllocatedUserdocFilter filter) {
        StringBuilder queryBuilder = new StringBuilder("select  pr.USERDOC_FILE_TYP as type, ft.FILE_TYPE_NAME from ip_proc pr\n" +
                "inner join ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES puc on puc.PROC_TYP = pr.PROC_TYP and puc.PROC_NBR = pr.PROC_NBR\n" +
                "inner join CF_FILE_TYPE ft on ft.FILE_TYP = pr.USERDOC_FILE_TYP");

        queryBuilder .append(" where (pr.USERDOC_FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"')) ");

        queryBuilder.append(excludedUserdocTypesCondition());
        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()){
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }
        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND puc.NEW_RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }

        queryBuilder.append(" group by pr.USERDOC_FILE_TYP , ft.FILE_TYPE_NAME order by ft.FILE_TYPE_NAME ");

        Query query = em.createNativeQuery(queryBuilder.toString());

        Integer responsibleUser = filter.getResponsibleUser();
        if (!StringUtils.isEmpty(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

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



    private List<NewlyAllocatedUserdocSimpleResult> fillResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        return resultList.stream().map(object->new NewlyAllocatedUserdocSimpleResult(
                new CDocumentId((String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue()),(Date) object[4],
                (String) object[5],(String) object[6],
                new UserdocIpObject((object[13]==null?null:object[13].toString()),(String) object[14],
                        new CFileId((String) object[9],(String) object[10],((BigDecimal) object[11]).intValue(),((BigDecimal) object[12]).intValue()),null,(String) object[15]),
                (String) object[8],(Date) object[7])).collect(Collectors.toList());

    }


    private String excludedUserdocTypesCondition(){
        return " and pr.USERDOC_TYP not in ('ЕПИВ','ЕПЛИЦ','ЕПНИЛ','ЕПОПО','ЕПВПР') ";
    }

    private String buildQuery(NewlyAllocatedUserdocFilter filter, boolean isCount) {

        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " c.*  ");
        queryBuilder.append(" from ( select TOP 500 d.DOC_ORI,d.DOC_LOG,d.DOC_SER,d.DOC_NBR,d.FILING_DATE,(case\n" +
                "    when d.EXTERNAL_SYSTEM_ID is null then CONCAT(d.DOC_ORI,'/', d.DOC_LOG,'/',convert(varchar(max), d.DOC_SER),'/',convert(varchar(max), d.DOC_NBR))  else d.EXTERNAL_SYSTEM_ID end) as registrationNumber,\n" +
                "       ut.USERDOC_NAME,puc.DATE_CHANGED,us.USER_NAME,pr.USERDOC_FILE_SEQ,pr.USERDOC_FILE_TYP,pr.USERDOC_FILE_SER,pr.USERDOC_FILE_NBR,(case when f.REGISTRATION_NBR is not null AND f.FILE_TYP!='T' then concat(f.REGISTRATION_NBR,f.REGISTRATION_DUP)\n" +
                "    else convert(varchar(10),f.REGISTRATION_NBR) end) as REGISTRATION_NBR,f.TITLE, s2.STATUS_NAME as objectStatusName ");

        queryBuilder.append(" from ip_proc pr\n" +
                "inner join ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES puc on puc.PROC_TYP = pr.PROC_TYP and puc.PROC_NBR = pr.PROC_NBR\n" +
                "inner join ip_doc d on d.DOC_NBR = pr.DOC_NBR and d.DOC_LOG = pr.DOC_LOG and d.DOC_SER = pr.DOC_SER and d.DOC_ORI = pr.DOC_ORI\n" +
                "inner join  IPASPROD.CF_USERDOC_TYPE ut on ut.USERDOC_TYP = pr.USERDOC_TYP\n" +
                "inner join IP_USER us on us.USER_ID = puc.NEW_RESPONSIBLE_USER_ID\n" +
                "JOIN IPASPROD.IP_FILE f on f.FILE_NBR=pr.USERDOC_FILE_NBR and f.FILE_SER = pr.USERDOC_FILE_SER and f.FILE_TYP = pr.USERDOC_FILE_TYP and f.FILE_SEQ = pr.USERDOC_FILE_SEQ ");
        queryBuilder.append(" JOIN IPASPROD.IP_PROC p2 on p2.PROC_NBR = pr.FILE_PROC_NBR and p2.PROC_TYP = pr.FILE_PROC_TYP");
        queryBuilder.append(" JOIN IPASPROD.CF_STATUS s2 ON p2.PROC_TYP = s2.PROC_TYP AND p2.STATUS_CODE = s2.STATUS_CODE");
        queryBuilder .append(" where (pr.USERDOC_FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"')) ");
        queryBuilder.append(excludedUserdocTypesCondition());
        if (!StringUtils.isEmpty(filter.getUserdocType())) {
            queryBuilder.append(" AND pr.USERDOC_TYP = :userdocType ");
        }

        if (Objects.nonNull(filter.getUserdocFilingNumber()) && !filter.getUserdocFilingNumber().isEmpty()){
            queryBuilder.append(" AND (d.EXTERNAL_SYSTEM_ID like :userdocFilingNumber or " +
                    " CONCAT(d.DOC_ORI, d.DOC_LOG,d.DOC_SER,d.DOC_NBR) like :userdocFilingNumber)  ");
        }

        if(Objects.nonNull(filter.getUserdocFilingDateFrom())){
            queryBuilder.append(" AND d.FILING_DATE >= :userdocFilingDateFrom ");
        }
        if(Objects.nonNull(filter.getUserdocFilingDateTo())){
            queryBuilder.append(" AND d.FILING_DATE <= :userdocFilingDateTo ");
        }

        if(Objects.nonNull(filter.getObjectFileTyp()) && !filter.getObjectFileTyp().isEmpty()){
            queryBuilder.append(" AND pr.USERDOC_FILE_TYP = :objectFileTyp ");
        }

        if (Objects.nonNull(filter.getObjectFileNbr()) && !filter.getObjectFileNbr().isEmpty()){
            queryBuilder.append(" AND pr.USERDOC_FILE_NBR like :objectFileNbr ");
        }

        if (Objects.nonNull(filter.getResponsibleUser())) {
            queryBuilder.append(" AND puc.NEW_RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        }

        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()){
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = NewlyAllocatedUserdocSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        queryBuilder.append(" ) c ");
        return queryBuilder.toString();

    }


    private String constructConditionsRelatedToRoles(){
        StringBuilder queryBuilder = new StringBuilder();
        if (!SecurityUtils.hasRights(SecurityRole.MarkViewOwn)){
            queryBuilder.append(" AND pr.USERDOC_FILE_TYP not in ("+ BasicUtils.getMarkRelatedFileTypesAsSequence()+") ");
        }
        if (!SecurityUtils.hasRights(SecurityRole.PatentViewOwn)){
            queryBuilder.append(" AND pr.USERDOC_FILE_TYP not in ("+BasicUtils.getPatentRelatedFileTypesAsSequence()+") ");
        }
        return queryBuilder.toString();
    }


    private void addQueryParams(NewlyAllocatedUserdocFilter filter, Query query) {

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


        String userdocType = filter.getUserdocType();
        if (!StringUtils.isEmpty(userdocType)) {
            query.setParameter("userdocType", userdocType);
        }

        Integer responsibleUser = filter.getResponsibleUser();
        if (Objects.nonNull(responsibleUser)) {
            query.setParameter("responsibleUsers", userService.getDepartmentAndAuthorizedByUserIds(responsibleUser));
        }

    }

}
