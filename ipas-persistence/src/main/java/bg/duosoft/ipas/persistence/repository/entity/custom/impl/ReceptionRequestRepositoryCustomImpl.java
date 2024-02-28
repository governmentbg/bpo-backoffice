package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.core.service.reception.SubmissionTypeService;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.enums.security.SecurityRole;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import bg.duosoft.ipas.persistence.model.nonentity.IPObjectSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.ReceptionRequestSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.ReceptionRequestRepositoryCustom;
import bg.duosoft.ipas.util.filter.ReceptionListFilter;
import bg.duosoft.ipas.util.filter.sorter.ReceptionSorterUtils;
import bg.duosoft.ipas.util.general.BasicUtils;
import bg.duosoft.ipas.util.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReceptionRequestRepositoryCustomImpl extends BaseRepositoryCustomImpl implements ReceptionRequestRepositoryCustom {

    @Autowired
    private SubmissionTypeService submissionTypeService;


    private List<ReceptionRequestSimpleResult> fillResult(Query query) {
        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(objects -> {
                    Integer id =((BigDecimal)objects[0]).intValue();
                    String fileSeq = (String) objects[1];
                    String fileType =(String) objects[2];
                    Integer fileSer =((BigDecimal)objects[3]).intValue();
                    Integer fileNbr = ((BigDecimal)objects[4]).intValue();
                    Integer externalId = ((BigDecimal)objects[5]).intValue();
                    Boolean originalExpected = (Boolean) objects[6];
                    Date filingDate =(Date)objects[7];
                    String name  =(String)objects[8];
                    String notes = (String)objects[9];
                    Integer status =(Integer)objects[10];
                    Date createDate =(Date)objects[11];
                    Integer submissionTypeId = ((BigDecimal)objects[12]).intValue();
                    String submissionTypeName = submissionTypeService.selectById(submissionTypeId).getName();
                    String procTyp = (String)objects[13];
                    String procNbrAsStr = ((BigDecimal)objects[14]).toString();
                    String procId = procTyp.concat("-").concat(procNbrAsStr);
                    String title = (String)objects[15];
                    String priorityRequest = (String)objects[16];
                    return new ReceptionRequestSimpleResult(
                            id, fileSeq, fileType, fileSer, fileNbr, externalId,originalExpected, filingDate, name, notes, status,createDate,submissionTypeName,procId,title,priorityRequest);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ReceptionRequestSimpleResult> getReceptionsWithoutStatus(ReceptionListFilter filter) {
        String buildQuery = buildQuery(filter, false,false);
        Query query = em.createNativeQuery(buildQuery);
        return fillResult(query);
    }

    @Override
    public List<ReceptionRequestSimpleResult> getFirstReceptionsWithoutStatus() {
        ReceptionListFilter receptionListFilter = new ReceptionListFilter(ReceptionListFilter.ASC_ORDER, ReceptionSorterUtils.RECEPTION_CREATE_DATE);
        String buildQuery = buildQuery(receptionListFilter, false,true);
        Query query = em.createNativeQuery(buildQuery);
        return fillResult(query);
    }

    @Override
    public Integer getReceptionsWithoutStatusCount() {
        String buildQuery = buildQuery(null, true,true);
        Query query = em.createNativeQuery(buildQuery);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    private String buildQuery(ReceptionListFilter filter, boolean isCount,boolean isHomePanel) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        if (isCount){
            queryBuilder.append(" COUNT(*) ");
        }
        else{
            if (isHomePanel){
                queryBuilder.append(" top 5 ");
            }
            queryBuilder.append(" r.ID, r.FILE_SEQ, r.FILE_TYP, r.FILE_SER, r.FILE_NBR, r.EXTERNAL_ID, r.ORIGINAL_EXPECTED,r.FILING_DATE " +
                    ",r.NAME,r.NOTES,r.STATUS,r.CREATE_DATE,r.SUBMISSION_TYPE,prc.PROC_TYP,prc.PROC_NBR, COALESCE(f.TITLE,r.NAME) as title , CASE WHEN efd.PRIORITY_REQUEST = 1 THEN '1' ELSE null END AS priorityRequest ");
        }
        queryBuilder .append(" FROM EXT_RECEPTION.RECEPTION_REQUEST r inner join IP_FILE f on f.FILE_NBR = r.FILE_NBR and f.FILE_SER = r.FILE_SER and f.FILE_SEQ = r.FILE_SEQ and f.FILE_TYP = r.FILE_TYP\n" +
                " inner join IP_PROC prc on f.PROC_NBR = prc.PROC_NBR and f.PROC_TYP = prc.PROC_TYP ")
                .append(" LEFT JOIN ext_core.IP_OBJECT_EFILING_DATA efd on efd.FILE_SEQ = r.file_seq and efd.FILE_TYP = r.file_typ and efd.FILE_SER = r.file_ser and efd.FILE_NBR = r.FILE_NBR ")
        .append(" WHERE 1=1 ")
        .append(" AND (r.STATUS IS NULL or prc.RESPONSIBLE_USER_ID is null) ").append( " and r.FILE_TYP not in ('"+ FileType.SINGLE_DESIGN.code()+"'"+",'"+FileType.INTERNATIONAL_SINGLE_DESIGN.code()+"') ");

        String additionalConditionsRelatedToRoles = constructConditionsRelatedToRoles();
        if (!additionalConditionsRelatedToRoles.isEmpty()){
            queryBuilder.append(additionalConditionsRelatedToRoles);
        }
        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = ReceptionSorterUtils.receptionSorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }

        return queryBuilder.toString();
    }

    private String constructConditionsRelatedToRoles(){
        StringBuilder queryBuilder = new StringBuilder();
        if (!SecurityUtils.hasRights(SecurityRole.MarkViewOwn)){
            queryBuilder.append(" AND r.FILE_TYP not in ("+BasicUtils.getMarkRelatedFileTypesAsSequence()+") ");
        }
        if (!SecurityUtils.hasRights(SecurityRole.PatentViewOwn)){
            queryBuilder.append(" AND r.FILE_TYP not in ("+BasicUtils.getPatentRelatedFileTypesAsSequence()+") ");
        }
       return queryBuilder.toString();
    }


}
