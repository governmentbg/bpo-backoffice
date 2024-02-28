package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.search.OffidocNotificationSearchParam;
import bg.duosoft.ipas.persistence.model.nonentity.OffidocNotification;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.OffidocNotificationRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Raya
 * Date: 11.05.2022
 * Time: 13:49
 */
@Repository
public class OffidocNotificationRepositoryImpl extends BaseRepositoryCustomImpl implements OffidocNotificationRepository {

    private static final String SELECT_COUNT_SQL_PART = " SELECT count(DISTINCT CONCAT(doc.OFFIDOC_ORI , CAST(doc.OFFIDOC_SER as varchar) , CAST(doc.OFFIDOC_NBR as varchar))) ";

    private static final String CASE_SQL_PART =
            " CASE "+
            "   WHEN doc.PORTAL_NOTIFICATION_READ_DATE is null and doc.EMAIL_NOTIFICATION_READ_DATE is null THEN DATEDIFF(day,  doc.NOTIFICATION_FINISHED_DATE, GETDATE()) "+
            "   WHEN doc.PORTAL_NOTIFICATION_READ_DATE is null and doc.EMAIL_NOTIFICATION_READ_DATE is not null THEN DATEDIFF(day,  doc.NOTIFICATION_FINISHED_DATE, doc.EMAIL_NOTIFICATION_READ_DATE) "+
            "   WHEN doc.PORTAL_NOTIFICATION_READ_DATE is not null and doc.EMAIL_NOTIFICATION_READ_DATE is null THEN DATEDIFF(day,  doc.NOTIFICATION_FINISHED_DATE, doc.PORTAL_NOTIFICATION_READ_DATE) "+
            "   WHEN doc.PORTAL_NOTIFICATION_READ_DATE is not null and doc.EMAIL_NOTIFICATION_READ_DATE is not null and doc.PORTAL_NOTIFICATION_READ_DATE > doc.EMAIL_NOTIFICATION_READ_DATE THEN DATEDIFF(day,  doc.NOTIFICATION_FINISHED_DATE, doc.EMAIL_NOTIFICATION_READ_DATE) "+
            "   WHEN doc.PORTAL_NOTIFICATION_READ_DATE is not null and doc.EMAIL_NOTIFICATION_READ_DATE is not null and doc.PORTAL_NOTIFICATION_READ_DATE < doc.EMAIL_NOTIFICATION_READ_DATE THEN DATEDIFF(day,  doc.NOTIFICATION_FINISHED_DATE, doc.PORTAL_NOTIFICATION_READ_DATE) "+
            " END";

    private static final String SELECT_DATA_SQL_PART = " SELECT DISTINCT doc.OFFIDOC_ORI as offidocOri, doc.OFFIDOC_SER as offidocSer, doc.OFFIDOC_NBR as offidocNbr, doc.CLOSEST_MAIN_PARENT_OBJECT_ID as closestParentObjectId, doc.REGISTRATION_NUMBER as registrationNumber, doc.NOTIFICATION_FINISHED_DATE as dateFinished, " +
            " doc.EMAIL_NOTIFICATION_READ_DATE as dateReadEmail, doc.PORTAL_NOTIFICATION_READ_DATE as dateReadPortal, cf.OFFIDOC_NAME as offidocTypeName, "+
            CASE_SQL_PART + " as days, "+
            " objEf.LOG_USER_NAME as objectEfilingUser, "+
            " userdocEf.LOG_USER_NAME as userdocEfilingUser ";

    private static final String FROM_WHERE_SQL_PART = " FROM EXT_CORE.IP_OFFIDOC_ABDOCS_DOCUMENT doc "+
            " JOIN IP_OFFIDOC ipdoc on doc.OFFIDOC_ORI = ipdoc.OFFIDOC_ORI and doc.OFFIDOC_SER = ipdoc.OFFIDOC_SER and doc.OFFIDOC_NBR = ipdoc.OFFIDOC_NBR "+
            " JOIN IP_ACTION act on act.PROC_NBR = ipdoc.PROC_NBR and act.PROC_TYP = ipdoc.PROC_TYP and act.ACTION_NBR = ipdoc.ACTION_NBR "+
            " JOIN CF_OFFIDOC_TYPE cf on ipdoc.OFFIDOC_TYP = cf.OFFIDOC_TYP "+
            " LEFT JOIN EXT_CORE.IP_OBJECT_EFILING_DATA objEf on doc.CLOSEST_MAIN_PARENT_OBJECT_ID = CONCAT(objEf.FILE_SEQ, '/', objEf.FILE_TYP, '/', objEf.FILE_SER, '/', objEf.FILE_NBR) "+
            " LEFT JOIN EXT_CORE.IP_USERDOC_EFILING_DATA userdocEf on doc.CLOSEST_MAIN_PARENT_OBJECT_ID = CONCAT(userdocEf.DOC_ORI,'/',userdocEf.DOC_LOG,'/',userdocEf.DOC_SER,'/',userdocEf.DOC_NBR) "+
            " WHERE doc.NOTIFICATION_FINISHED_DATE is not null ";

    @Override
    public List<OffidocNotification> findOffidocNotifications(OffidocNotificationSearchParam searchParam) {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParams = new HashMap<>();

        createQueryAndAddParams(searchParam, false, queryBuilder, queryParams);
        Query query = em.createNativeQuery(queryBuilder.toString());
        queryParams.keySet().stream().forEach(key -> query.setParameter(key, queryParams.get(key)));
        query.setMaxResults(searchParam.getPageSize());
        query.setFirstResult((searchParam.getPage() - 1) * searchParam.getPageSize());
        List<Object[]> result = query.getResultList();

        return transformResult(result);
    }

    @Override
    public Integer countOffidocNotifications(OffidocNotificationSearchParam searchParam) {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParams = new HashMap<>();
        createQueryAndAddParams(searchParam, true, queryBuilder, queryParams);
        Query query = em.createNativeQuery(queryBuilder.toString());
        queryParams.keySet().stream().forEach(key -> query.setParameter(key, queryParams.get(key)));
        Integer result = (Integer)query.getSingleResult();
        return result;
    }

    private List<OffidocNotification> transformResult(List<Object[]> result){
        List<OffidocNotification> transformed = result.stream().map(arr -> {
            OffidocNotification notification = new OffidocNotification();
            int index = 0;
            notification.setOffidocOri((String) arr[index++]);
            notification.setOffidocSer(((BigDecimal) arr[index++]).intValue());
            notification.setOffidocNbr(((BigDecimal) arr[index++]).intValue());
            notification.setClosestParentObjectId((String) arr[index++]);
            notification.setRegistrationNumber((String) arr[index++]);
            notification.setDateFinished((Date) arr[index++]);
            notification.setDateReadEmail((Date) arr[index++]);
            notification.setDateReadPortal((Date) arr[index++]);
            notification.setOffidocTypeName((String)arr[index++]);
            notification.setDays((Integer)arr[index++]);
            notification.setObjectEfilingUser((String) arr[index++]);
            notification.setUserdocEfilingUser((String) arr[index++]);
            return notification;
        }).collect(Collectors.toList());
        return transformed;
    }

    private void createQueryAndAddParams(OffidocNotificationSearchParam searchParam, boolean isCount, StringBuilder queryBuilder, Map<String, Object> queryParams){
        if(isCount){
            queryBuilder.append(SELECT_COUNT_SQL_PART).append(FROM_WHERE_SQL_PART);
        } else {
            queryBuilder.append(SELECT_DATA_SQL_PART).append(FROM_WHERE_SQL_PART);
        }

        if(searchParam.getRegistrationNumber() != null && !searchParam.getRegistrationNumber().isEmpty()){
            queryBuilder.append(" and doc.REGISTRATION_NUMBER like :registrationNumber ESCAPE '\\'");
            String escapedRegNum = searchParam.getRegistrationNumber().trim().replaceAll("\\[", "\\\\[").replaceAll("]", "\\\\]");

            queryParams.put("registrationNumber", "%"+escapedRegNum+"%");
        }


        if(searchParam.isShowReadByEmail() || searchParam.isShowReadInPortal() || searchParam.isShowUnread()){
            queryBuilder.append(" and ( ");
            boolean statementAdded = false;
            if(searchParam.isShowReadByEmail()) {
                queryBuilder.append(" doc.EMAIL_NOTIFICATION_READ_DATE is not null ");
                statementAdded = true;
            }
            if(searchParam.isShowReadInPortal()){
                if(statementAdded){
                    queryBuilder.append(" or ");
                }
                queryBuilder.append(" doc.PORTAL_NOTIFICATION_READ_DATE is not null ");
                statementAdded = true;
            }
            if(searchParam.isShowUnread()){
                if(statementAdded){
                    queryBuilder.append(" or ");
                }
                queryBuilder.append(" (doc.EMAIL_NOTIFICATION_READ_DATE is null and doc.PORTAL_NOTIFICATION_READ_DATE is null) ");
            }
            queryBuilder.append(" ) ");
        } else {
            queryBuilder.append(" and 1=2 ");
        }

        if(!(searchParam.isEFiled() && searchParam.isNonEFiled())) {
            if (searchParam.isEFiled()) {
                queryBuilder.append(" and (objEf.LOG_USER_NAME is not null or userdocEf.LOG_USER_NAME is not null) ");
            } else {
                queryBuilder.append(" and (objEf.LOG_USER_NAME is null and userdocEf.LOG_USER_NAME is null) ");
            }
            if (searchParam.isNonEFiled()) {
                queryBuilder.append(" and (objEf.LOG_USER_NAME is null and userdocEf.LOG_USER_NAME is null) ");
            } else {
                queryBuilder.append(" and (objEf.LOG_USER_NAME is not null or userdocEf.LOG_USER_NAME is not null) ");
            }
        }
        if(searchParam.getCaptureUserId() != null){
            queryBuilder.append(" and act.CAPTURE_USER_ID = :userId");
            queryParams.put("userId", searchParam.getCaptureUserId());
        }
        if(searchParam.getDateFinishedFrom() != null){
            queryBuilder.append(" and doc.NOTIFICATION_FINISHED_DATE > :dateFinishedFrom ");
            queryParams.put("dateFinishedFrom", searchParam.getDateFinishedFrom());
        }

        if(searchParam.getDateFinishedTo() != null){
            queryBuilder.append(" and doc.NOTIFICATION_FINISHED_DATE < :dateFinishedTo ");
            queryParams.put("dateFinishedTo", searchParam.getDateFinishedTo());
        }
        if(searchParam.getDateReadFrom() != null){
            queryBuilder.append(" and (doc.EMAIL_NOTIFICATION_READ_DATE > :dateReadFrom or doc.PORTAL_NOTIFICATION_READ_DATE > :dateReadFrom) ");
            queryParams.put("dateReadFrom", searchParam.getDateReadFrom());
        }
        if(searchParam.getDateReadTo() != null){
            queryBuilder.append(" and (doc.EMAIL_NOTIFICATION_READ_DATE < :dateReadTo or doc.PORTAL_NOTIFICATION_READ_DATE < :dateReadTo) ");
            queryParams.put("dateReadTo", searchParam.getDateReadTo());
        }
        if(searchParam.getNoOlderThanDays() != null){
            queryBuilder.append(" and  ").append(CASE_SQL_PART).append(" < :days ");
            queryParams.put("days", searchParam.getNoOlderThanDays());
        }

        if(searchParam.getObjectType() != null && !searchParam.getObjectType().isEmpty()){
            queryBuilder.append(" and  doc.CLOSEST_MAIN_PARENT_OBJECT_ID like :objectType");
            queryParams.put("objectType", "%/"+searchParam.getObjectType()+"/%");
        }

        if(!isCount && searchParam.getSortColumn() != null){
            queryBuilder.append(" ORDER BY ").append(getSqlSortColumn(searchParam.getSortColumn()));
            queryBuilder.append(searchParam.getSortOrder() != null? searchParam.getSortOrder(): " ASC ");
        }
    }

    private String getSqlSortColumn(String sortColumn){
        switch (sortColumn){
            case OffidocNotificationSearchParam.NOTIFICATION_FINISHED_DATE: return " doc.NOTIFICATION_FINISHED_DATE ";
            case OffidocNotificationSearchParam.EMAIL_NOTIFICATION_READ_DATE: return " doc.EMAIL_NOTIFICATION_READ_DATE ";
            case OffidocNotificationSearchParam.PORTAL_NOTIFICATION_READ_DATE: return " doc.PORTAL_NOTIFICATION_READ_DATE ";
            default: return " days ";
        }
    }
}
