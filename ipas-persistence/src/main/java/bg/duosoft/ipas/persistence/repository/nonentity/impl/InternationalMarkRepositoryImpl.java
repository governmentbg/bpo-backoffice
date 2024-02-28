package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.persistence.model.nonentity.IPMarkSimpleResult;
import bg.duosoft.ipas.persistence.model.nonentity.InternationalMarkSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.InternationalMarkRepository;
import bg.duosoft.ipas.util.filter.InternationalMarkFilter;
import bg.duosoft.ipas.util.filter.sorter.InternationalMarkSorterUtils;
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
public class InternationalMarkRepositoryImpl extends BaseRepositoryCustomImpl implements InternationalMarkRepository {
    @Override
    public List<IPMarkSimpleResult> getInternationalMarksList(InternationalMarkFilter filter) {
        String buildQuery = buildQuery(filter, false);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter,query);
        query.setMaxResults(filter.getPageSize());
        query.setFirstResult((filter.getPage() - 1) * filter.getPageSize());

        List<Object[]> resultList = query.getResultList();

        return resultList.stream()
                .map(objects -> {
                    String filingNumber = (String) objects[0];
                    String[] filingNumberParts = filingNumber.split("/");
                    Date filingDate = (Date) objects[1];
                    String title = (String) objects[2];
                    Date receptionDate = (Date) objects[4];
                    String transactionType = (String) objects[5];
                    String registrationNumber = (String) objects[8];
                    String responsibleUserName = (String) objects[9];
                    String statusName = (String) objects[10];
                    String niceClasses = (String) objects[11];
                    return new IPMarkSimpleResult(
                            filingNumberParts[0], filingNumberParts[1], Integer.valueOf(filingNumberParts[2]),
                            Integer.valueOf(filingNumberParts[3]), registrationNumber, title, null, statusName,
                            filingDate, null, null, filingNumber,null,null,null,responsibleUserName,transactionType,receptionDate,
                            ((String)objects[6]).concat("-").concat(((BigDecimal)objects[7]).toString()),null,null,null, null, null, niceClasses);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Integer getInternationalMarksCount(InternationalMarkFilter filter) {
        String buildQuery = buildQuery(filter, true);
        Query query = em.createNativeQuery(buildQuery);
        addQueryParams(filter,query);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public List<InternationalMarkSimpleResult> selectInternationalMarksAutocompleteResult(Integer registrationNbr, String registrationDup, List<String> internationalFileTypes) {
        String buildQuery = selectAutocompleteResult(registrationNbr, registrationDup);
        Query query = em.createNativeQuery(buildQuery);

        query.setFirstResult(0);
        query.setMaxResults(1000);

        if (Objects.nonNull(registrationNbr)) {
            query.setParameter("registrationNumber", registrationNbr + "%");
        }
        if (StringUtils.hasText(registrationDup)) {
            query.setParameter("registrationDup", registrationDup);
        }

        List<Object[]> resultList = query.getResultList();
        return resultList.stream()
                .map(objects -> {
                    String registrationNumber = (String) objects[0];
                    String markName = (String) objects[1];
                    String fileId = (String) objects[2];
                    return new InternationalMarkSimpleResult(
                            registrationNumber, markName, fileId);
                })
                .collect(Collectors.toList());
    }

    @Override
    public InternationalMarkSimpleResult selectInternationalMarkByFileId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("SELECT concat(m.REGISTRATION_NBR, m.REGISTRATION_DUP) as registrationNbr, n.MARK_NAME, concat(m.FILE_SEQ, '/', m.FILE_TYP, '/', m.FILE_SER, '/', m.FILE_NBR) as file_id\n")
                .append(" FROM IP_MARK m left join IP_NAME n on m.MARK_CODE=n.MARK_CODE\n")
                .append(" WHERE m.FILE_TYP in ('I', 'R', 'B') AND m.FILE_SEQ = :fileSeq AND m.FILE_TYP = :fileType AND m.FILE_SER = :fileSer AND m.FILE_NBR = :fileNbr \n");

        Query query = em.createNativeQuery(queryBuilder.toString());
        query.setParameter("fileSeq", fileSeq);
        query.setParameter("fileType", fileType);
        query.setParameter("fileSer", fileSer);
        query.setParameter("fileNbr", fileNbr);

        List<Object[]> resultList = query.getResultList();

        if (!CollectionUtils.isEmpty(resultList)) {
            Object[] result = resultList.get(0);
            String registrationNumber = (String) result[0];
            String markName = (String) result[1];
            String fileId = (String) result[2];
            return new InternationalMarkSimpleResult(registrationNumber, markName, fileId);
        }

        return null;
    }

    private String selectAutocompleteResult(Integer registrationNbr, String registrationDup) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("SELECT concat(m.REGISTRATION_NBR, m.REGISTRATION_DUP) as registrationNbr, n.MARK_NAME, concat(m.FILE_SEQ, '/', m.FILE_TYP, '/', m.FILE_SER, '/', m.FILE_NBR) as file_id\n")
                .append(" FROM IP_MARK m left join IP_NAME n on m.MARK_CODE=n.MARK_CODE\n")
                .append(" WHERE m.FILE_TYP in ('I', 'R', 'B')\n");

        if (Objects.nonNull(registrationNbr)) {
            queryBuilder.append(" AND cast(m.REGISTRATION_NBR as varchar) like :registrationNumber\n");
        }

        if (StringUtils.hasText(registrationDup)) {
            queryBuilder.append(" AND m.REGISTRATION_DUP = :registrationDup\n");

        }
        queryBuilder.append(" ORDER BY m.REGISTRATION_NBR, m.REGISTRATION_DUP\n");
        return queryBuilder.toString();
    }

    private void addQueryParams(InternationalMarkFilter filter, Query query) {
        if (Objects.nonNull(filter.getGazno()) && !filter.getGazno().isEmpty()) {
            query.setParameter("gazno", filter.getGazno());
        }

    }

    private String buildQuery(InternationalMarkFilter filter, boolean isCount) {
        // THIS SQL SHOULD BE REPLACED (TEST PHASE).
        StringBuilder queryBuilder = new StringBuilder("SELECT ")
                .append(isCount ? " COUNT(*) " : " * ");
        queryBuilder.append(" FROM(select m.file_seq + '/' + m.file_typ + '/' + CAST(m.file_ser as VARCHAR(4)) + '/'+CAST(m.FILE_NBR as VARCHAR) as filingNumber,\n" +
                " cast(m.FILING_DATE as date)                  as FILING_DATE,\n" +
                " f.TITLE,\n" +
                " pc.STATUS_CODE,\n" +
                " d.RECEPTION_DATE,\n "+
                " eno.transaction_type,\n "+
                " pc.PROC_TYP ,\n "+
                " pc.PROC_NBR ,\n " +
                " (case when m.REGISTRATION_NBR is not null then concat(m.REGISTRATION_NBR,m.REGISTRATION_DUP)\n" +
                "    else convert(varchar(10),m.REGISTRATION_NBR) end) as REGISTRATION_NBR ,\n " +
                " us.USER_NAME, \n "+
                " cf.STATUS_NAME, \n " +
                " stuff((select ' ' + cast(t2.NICE_CLASS_CODE as varchar(2))\n" +
                "      from IP_MARK_NICE_CLASSES t2 where m.FILE_SEQ = t2.FILE_SEQ AND m.FILE_TYP=t2.FILE_TYP and m.FILE_SER=t2.FILE_SER and m.FILE_NBR=t2.FILE_NBR order by t2.NICE_CLASS_CODE\n" +
                "      for xml path('')),1,1,'') as NICE_CLASSES\n"+
                " from IP_MARK m\n" +
                "INNER JOIN IP_FILE f on f.FILE_TYP =m.FILE_TYP and f.FILE_NBR = m.FILE_NBR and f.FILE_SEQ = m.FILE_SEQ and f.FILE_SER = m.FILE_SER\n" +
                "INNER JOIN IP_PROC pc on pc.PROC_NBR = f.PROC_NBR and f.PROC_TYP = pc.PROC_TYP \n" +
                "INNER JOIN cf_status cf on pc.STATUS_CODE = cf.STATUS_CODE and pc.PROC_TYP = cf.PROC_TYP  \n" +
                "LEFT JOIN IP_USER us ON us.USER_ID = pc.RESPONSIBLE_USER_ID \n" +
                " INNER JOIN IP_DOC d on f.DOC_NBR = d.DOC_NBR and f.DOC_LOG = d.DOC_LOG and f.DOC_ORI = d.DOC_ORI and f.DOC_SER = d.DOC_SER \n" +
                " INNER JOIN EXT_CORE.ENOTIF_MARK eno on m.FILE_NBR = eno.FILE_NBR and m.FILE_SEQ = eno.FILE_SEQ and m.FILE_TYP = eno.FILE_TYP and m.FILE_SER = eno.FILE_SER \n"+
                " where 1=1");
        queryBuilder.append(getDividedRestrictionCriteria(filter));
        if (Objects.nonNull(filter.getGazno()) && !filter.getGazno().isEmpty()) {
            queryBuilder.append(" AND eno.GAZNO = :gazno ");
        }
        queryBuilder.append(" ) m ");

        if (!isCount) {
            String sortColumn = filter.getSortColumn();
            String sortOrder = filter.getSortOrder();
            String[] columns = InternationalMarkSorterUtils.sorterColumnMap().get(sortColumn).split(",");
            String order = String.join(" " + sortOrder + " , ", columns) + " " + sortOrder;
            queryBuilder.append(" ORDER BY ").append(order);
        }
        return queryBuilder.toString();
    }

    private String getDividedRestrictionCriteria(InternationalMarkFilter filter) {
        if (Objects.isNull(filter.getWithoutDivided()) || filter.getWithoutDivided()) {
            return " AND f.FILE_TYP in ('I','R')";
        } else {
            return " AND f.FILE_TYP in ('I','R','B')";
        }
    }
}
