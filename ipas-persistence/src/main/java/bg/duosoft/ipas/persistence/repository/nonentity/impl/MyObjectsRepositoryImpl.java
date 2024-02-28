package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.service.structure.UserService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.MyObjectsRepository;
import bg.duosoft.ipas.util.filter.MyObjectsFilter;
import bg.duosoft.ipas.util.filter.sorter.MyObjectsSorterUtils;
import bg.duosoft.ipas.util.home_page.HomePageUtils;
import org.aspectj.weaver.IClassFileProvider;
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
public class MyObjectsRepositoryImpl extends BaseRepositoryCustomImpl implements MyObjectsRepository {

    @Autowired
    private UserService userService;

    @Override
    public List<IPObjectSimpleResult> getMyObjectsList(MyObjectsFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());
        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(objects -> {
                    String filingNumber = (String) objects[0];
                    String status = (String) objects[1];
                    String statusCode = (String) objects[3];
                    String newlyAllocated = (String) objects[5];
                    String priorityRequest = (String) objects[6];
                    Date statusDate = (Date) objects[2];
                    String regNbr = (String) objects[7];
                    String title = objects[8]!=null ? (String) objects[8]:null;
                    String bordero = objects[9]!=null ? (String) objects[9]:null;
                    String journalCode = objects[10]!=null ? (String) objects[10]:null;
                    String[] filingNumberParts = filingNumber.split("/");
                    return new IPObjectSimpleResult(
                            filingNumberParts[0], filingNumberParts[1], Integer.valueOf(filingNumberParts[2]),
                            Integer.valueOf(filingNumberParts[3]), regNbr,  title, statusCode, status,
                            null, null, null, filingNumber,null,statusDate,null,
                            (String) objects[4],null,null,null,newlyAllocated,priorityRequest,null, bordero, journalCode);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getMyObjectsCount(MyObjectsFilter filter) {
       return getCount(filter,true);
    }

    private Integer getCount(MyObjectsFilter filter, boolean isCount){
        String buildQuery = buildQuery(filter, isCount);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter, query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(MyObjectsFilter filter, boolean isCount) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(isCount ? " COUNT(*) " : " prc.file_seq + '/' + prc.file_typ + '/' + CAST(prc.file_ser as VARCHAR(4)) + '/' +\n" +
                "       CAST(prc.FILE_NBR as VARCHAR) as filingNumber,\n" +
                "       cfs.STATUS_NAME                                    as status,\n" +
                "       prc.STATUS_DATE                                    as statusDate,\n" +
                "       prc.STATUS_CODE                                   as statusCode," +
                "   us.USER_NAME as userName, CASE WHEN ch.STATUS = 0 THEN '0' ELSE null END AS newlyAllocated ,CASE WHEN efd.PRIORITY_REQUEST = 1 THEN '1' ELSE null END AS priorityRequest,(case when fl.REGISTRATION_NBR is not null AND fl.FILE_TYP!='T' then concat(fl.REGISTRATION_NBR,fl.REGISTRATION_DUP)\n" +
                "    else convert(varchar(10),fl.REGISTRATION_NBR) end) as REGISTRATION_NBR,fl.TITLE, e.gazno,  a.JOURNAL_CODE ");
        queryBuilder.append(" FROM ipasprod.IP_PROC prc ");
        queryBuilder.append(" JOIN IP_FILE fl on fl.PROC_NBR = prc.PROC_NBR and fl.PROC_TYP = prc.PROC_TYP ");
        queryBuilder.append(" JOIN ipasprod.CF_STATUS cfs on prc.STATUS_CODE = cfs.STATUS_CODE and cfs.PROC_TYP = prc.PROC_TYP  ");
        queryBuilder.append(" LEFT JOIN IPASPROD.IP_USER us ON us.USER_ID = prc.RESPONSIBLE_USER_ID ");
        queryBuilder.append(" LEFT JOIN EXT_CORE.IP_PROC_RESPONSIBLE_USER_CHANGES ch on ch.PROC_NBR = prc.PROC_NBR and ch.PROC_TYP = prc.PROC_TYP ");
        queryBuilder.append(" LEFT JOIN ext_core.IP_OBJECT_EFILING_DATA efd on efd.FILE_SEQ = prc.file_seq and efd.FILE_TYP = prc.file_typ and efd.FILE_SER = prc.file_ser and efd.FILE_NBR = prc.FILE_NBR ");
        queryBuilder.append(" LEFT JOIN ext_core.ENOTIF_MARK e on e.FILE_SEQ = prc.file_seq and e.FILE_TYP = prc.file_typ and e.FILE_SER = prc.file_ser and e.FILE_NBR = prc.FILE_NBR ");
        queryBuilder.append(" LEFT JOIN IP_ACTION a on prc.PROC_NBR = a.PROC_NBR and prc.PROC_TYP = a.PROC_TYP and a.ACTION_NBR = " +
                " ( SELECT min(ACTION_NBR) from IP_ACTION ia" +
                " JOIN CF_ACTION_TYPE ca on ia.ACTION_TYP = ca.ACTION_TYP WHERE ia.PROC_TYP=a.PROC_TYP and ia.PROC_NBR=a.PROC_NBR and ca.JOURNAL_WFILE='IR_Applications'" +
                " group by ia.PROC_NBR, ia.PROC_TYP\n" +
                "        ) ");
        queryBuilder.append(" WHERE prc.file_seq is not null and " +
                "prc.file_typ is not null and " +
                "prc.file_ser is not null and " +
                "prc.FILE_NBR is not null ").append(" and prc.FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') ");;
        queryBuilder.append(" AND prc.RESPONSIBLE_USER_ID in (:responsibleUsers)  ");
        queryBuilder.append(" and ((ch.STATUS = 0 and ch.NEW_RESPONSIBLE_USER_ID  =  prc.RESPONSIBLE_USER_ID )");
        queryBuilder.append(" or ((ch.PROC_NBR is null)" +
                " or (ch.DATE_CHANGED = ( select MAX(ch2.DATE_CHANGED) from ext_core.IP_PROC_RESPONSIBLE_USER_CHANGES ch2" +
                " where ch2.PROC_NBR=ch.PROC_NBR and ch2.PROC_TYP=ch.PROC_TYP )) ) ");
        queryBuilder.append(") ");


        if (!CollectionUtils.isEmpty(filter.getFileTypes())){
            queryBuilder.append(" AND prc.file_typ in (:fileTypes)  ");
        }

        if (Objects.nonNull(filter.getStatusCode()) && !filter.getStatusCode().isEmpty()) {
            queryBuilder.append(" AND prc.STATUS_CODE = :statusCode ");
            queryBuilder.append(" AND prc.PROC_TYP = :procTyp ");
        }

        if (filter.getNewlyAllocated()){
            queryBuilder.append(" AND ch.STATUS = 0 ");
        }

        if (filter.getPriorityRequest()){
            queryBuilder.append(" AND efd.PRIORITY_REQUEST = 1 ");
        }

        if (StringUtils.hasText(filter.getBordero())) {
            queryBuilder.append(" AND e.gazno like :bordero ");
        }

        if (StringUtils.hasText(filter.getJournalCode())) {
            queryBuilder.append(" AND a.JOURNAL_CODE like :journalCode ");
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

    private void addQueryParams(MyObjectsFilter filter, Query query) {
        String statusCode = filter.getStatusCode();
        query.setParameter("responsibleUsers", HomePageUtils.getResponsibleUsersForMyObjectPanels(userService,filter.getResponsibleUser()));

        if (!CollectionUtils.isEmpty(filter.getFileTypes())){
            query.setParameter("fileTypes", filter.getFileTypes());
        }

        if (Objects.nonNull(statusCode) && !statusCode.isEmpty()) {
            String[] splitStatusVal = statusCode.split("-");
            query.setParameter("statusCode", splitStatusVal[1]);
            query.setParameter("procTyp", splitStatusVal[0]);
        }

        if (StringUtils.hasText(filter.getBordero())) {
            query.setParameter("bordero", "%"+filter.getBordero()+"%");
        }

        if (StringUtils.hasText(filter.getJournalCode())) {
            query.setParameter("journalCode", "%"+filter.getJournalCode()+"%");
        }
    }
}
