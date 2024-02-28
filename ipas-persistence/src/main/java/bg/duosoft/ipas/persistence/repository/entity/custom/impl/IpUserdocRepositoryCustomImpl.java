package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.enums.EuPatentReceptionType;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocHierarchyChildNode;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocIpDocSimpleResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpUserdocRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class IpUserdocRepositoryCustomImpl extends BaseRepositoryCustomImpl implements IpUserdocRepositoryCustom {
    private static final String USERDOC_HIERARCHY_SQL = "WITH p(PROC_NBR, PROC_TYP, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, UPPER_PROC_NBR, UPPER_PROC_TYP, USERDOC_FILE_SEQ, USERDOC_FILE_TYP, USERDOC_FILE_SER, USERDOC_FILE_NBR, STATUS_CODE, RESPONSIBLE_USER_ID) AS\n" +
            "                     (SELECT PROC_NBR, PROC_TYP, DOC_ORI, DOC_LOG, DOC_SER, DOC_NBR, UPPER_PROC_NBR, UPPER_PROC_TYP, USERDOC_FILE_SEQ, USERDOC_FILE_TYP, USERDOC_FILE_SER, USERDOC_FILE_NBR, STATUS_CODE, RESPONSIBLE_USER_ID\n" +
            "                      FROM IP_PROC\n" +
            "                      WHERE ${INNER_WHERE}\n" +
            "                      UNION ALL\n" +
            "                      SELECT plus1.PROC_NBR, plus1.PROC_TYP, plus1.DOC_ORI, plus1.DOC_LOG, plus1.DOC_SER, plus1.DOC_NBR, plus1.UPPER_PROC_NBR, plus1.UPPER_PROC_TYP, plus1.USERDOC_FILE_SEQ, plus1.USERDOC_FILE_TYP, plus1.USERDOC_FILE_SER, plus1.USERDOC_FILE_NBR, plus1.STATUS_CODE, plus1.RESPONSIBLE_USER_ID\n" +
            "                      FROM IP_PROC as plus1, p\n" +
            "                      WHERE p.PROC_NBR  = plus1.UPPER_PROC_NBR and p.PROC_TYP = plus1.UPPER_PROC_TYP and plus1.USERDOC_TYP is not null)\n" +
            "        SELECT p.*, ud.USERDOC_TYP, d.EXTERNAL_SYSTEM_ID, d.DOC_SEQ_TYP, d.DOC_SEQ_NBR, d.DOC_SEQ_SERIES, d.FILING_DATE, p.USERDOC_FILE_SEQ FILE_SEQ, p.USERDOC_FILE_TYP FILE_TYP, p.USERDOC_FILE_SER FILE_SER, p.USERDOC_FILE_NBR FILE_NBR, udf.LOG_USER_NAME FROM p\n" +
            "            join IP_USERDOC_TYPES ud on ud.DOC_ORI = p.DOC_ORI and ud.DOC_LOG = p.DOC_LOG and ud.DOC_SER = p.DOC_SER and ud.DOC_NBR = p.DOC_NBR\n" +
            "            join IP_DOC d on ud.DOC_NBR = d.DOC_NBR and ud.DOC_ORI = d.DOC_ORI and ud.DOC_LOG = d.DOC_LOG and ud.DOC_SER = d.DOC_SER\n" +
            "            left join ext_core.IP_USERDOC_EFILING_DATA udf on udf.DOC_ORI = p.DOC_ORI and udf.DOC_LOG = p.DOC_LOG  and udf.DOC_SER = p.DOC_SER and udf.DOC_NBR = p.DOC_NBR\n" +
            "WHERE ${OUTER_WHERE}\n" +
            "order by d.filing_date\n";
    private static final String MAIN_EUPATENT_REQUEST_FOR_VALIDATION_SQL = "select top 1 udp2.DOC_ORI, udp2.DOC_LOG, udp2.DOC_SER, udp2.DOC_NBR from IP_USERDOC_PROCS udp\n" +
            "join IP_USERDOC_PROCS udp2 on udp.USERDOC_FILE_SEQ = udp2.USERDOC_FILE_SEQ and udp.USERDOC_FILE_TYP = udp2.USERDOC_FILE_TYP and udp.USERDOC_FILE_SER = udp2.USERDOC_FILE_SER and udp.USERDOC_FILE_NBR = udp2.USERDOC_FILE_NBR\n" +
            "join IP_PROC p on p.PROC_TYP = udp2.PROC_TYP and p.PROC_NBR = udp2.PROC_NBR\n" +
            "join IP_DOC d on d.DOC_ORI = udp2.DOC_ORI and d.DOC_LOG = udp2.DOC_LOG and d.DOC_SER = udp2.DOC_SER and d.DOC_NBR = udp2.DOC_NBR\n" +
            "join cf_status s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE and s.PROCESS_RESULT_TYP<>'X'\n" +
            "where udp.DOC_ORI = :docOri and udp.DOC_LOG = :docLog and udp.DOC_SER = :docSer and udp.DOC_NBR = :docNbr\n" +
            "and udp2.USERDOC_TYP in :userdocTypes\n" +
            "order by d.FILING_DATE, p.CREATION_DATE, d.doc_nbr\n";

    @Override
    public void changeUserdocPosition(String userdocProcTyp, Integer userdocProcNbr, String newUpperProcTyp, Integer newUpperProcNbr, Integer userChanged){
        StoredProcedureQuery query = this.em.createStoredProcedureQuery("changeUserDocumentUpperProcess");
        query.registerStoredProcedureParameter("userdocProcTyp", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("userdocProcNbr", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newUpperProcTyp", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("newUpperProcNbr", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("changeUserId", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("executionStatus", Boolean.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("errorMessage", String.class, ParameterMode.OUT);

        query.setParameter("userdocProcTyp", userdocProcTyp);
        query.setParameter("userdocProcNbr", userdocProcNbr);
        query.setParameter("newUpperProcTyp", newUpperProcTyp);
        query.setParameter("newUpperProcNbr", newUpperProcNbr);
        query.setParameter("changeUserId", userChanged);

        query.execute();

        Boolean errorExists = (Boolean) query.getOutputParameterValue("executionStatus");
        String errorMessage = (String) query.getOutputParameterValue("errorMessage");
        if (Objects.nonNull(errorExists) && errorExists) {
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    public List<UserdocIpDocSimpleResult> selectUserdocsFromAcstre() {
        String buildQuery = buildQuery();
        Query query = em.createNativeQuery(buildQuery);
        return fillResult(query);
    }

    private String buildQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT u.DOC_ORI, u.DOC_LOG, u.DOC_SER, u.DOC_NBR, d.EXTERNAL_SYSTEM_ID ")
                .append("FROM IPASPROD.IP_USERDOC u ")
                .append("JOIN IPASPROD.IP_DOC d ")
                .append("on u.DOC_NBR = d.DOC_NBR and u.DOC_ORI = d.DOC_ORI and u.DOC_LOG = d.DOC_LOG ")
                .append("and u.DOC_SER = d.DOC_SER ")
                .append("where d.EXTERNAL_SYSTEM_ID is not null ")
                .append("and d.DOC_SER >= 2016 ")
                .append("and d.EXTERNAL_SYSTEM_ID = (SELECT concat(u.DOC_ORI,'/', u.DOC_LOG,'/', u.DOC_SER,'/', d.DOC_SEQ_NBR)) ")
                .append(" ORDER BY u.DOC_ORI, u.DOC_LOG, u.DOC_SER, u.DOC_NBR ");
        return builder.toString();
    }

    private List<UserdocIpDocSimpleResult> fillResult(Query query) {
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }

        return resultList.stream()
                .map(object -> new UserdocIpDocSimpleResult(
                        new CDocumentId((String) object[0], (String) object[1], ((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).intValue()), (String) object[4]
                ))
                .collect(Collectors.toList());
    }


    public List<UserdocHierarchyChildNode> getUserdocUserdocHierarchy(String docOri, String docLog, Integer docSer, Integer docNbr) {
        StrSubstitutor substitutor = new StrSubstitutor(Map.of("INNER_WHERE", "DOC_ORI = :docOri and DOC_LOG = :docLog and DOC_SER = :docSer and DOC_NBR = :docNbr", "OUTER_WHERE", "not (p.DOC_ORI = :docOri and p.DOC_LOG = :docLog and p.DOC_SER = :docSer and p.DOC_NBR = :docNbr)"));
        String sql = substitutor.replace(USERDOC_HIERARCHY_SQL);
        Query q = em.createNativeQuery(sql, UserdocHierarchyChildNode.class);
        setDocumentIdParams(docOri, docLog, docSer, docNbr, q);
        return q.getResultList();
    }
    public List<UserdocHierarchyChildNode> getFileUserdocHierarchy(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        StrSubstitutor substitutor = new StrSubstitutor(Map.of("INNER_WHERE", "FILE_SEQ = :fileSeq and FILE_TYP = :fileTyp and FILE_SER = :fileSer and FILE_NBR = :fileNbr", "OUTER_WHERE", "1 = 1"));
        String sql = substitutor.replace(USERDOC_HIERARCHY_SQL);
        Query q = em.createNativeQuery(sql, UserdocHierarchyChildNode.class);
        setFileIdParams(fileSeq, fileTyp, fileSer, fileNbr, q);
        return q.getResultList();
    }

    @Override
    public boolean isMainEpoPatentRequestForValidation(String docOri, String docLog, Integer docSer, Integer docNbr) {
        String sql = MAIN_EUPATENT_REQUEST_FOR_VALIDATION_SQL;
        Query q = em.createNativeQuery(sql);
        setDocumentIdParams(docOri, docLog, docSer, docNbr, q);
        q.setParameter("userdocTypes", Arrays.asList(EuPatentReceptionType.VALIDATION.code(), EuPatentReceptionType.TEMPORARY_PROTECTION.code(), EuPatentReceptionType.VALIDATION_AFTER_MODIFICATION.code()));
        List<Object[]> list = q.getResultList();
        Object[] res = list.size() == 1 ? list.get(0) : null;
        return res != null && Objects.equals(new IpDocPK(docOri, docLog, docSer, docNbr), new IpDocPK((String)res[0], (String)res[1], ((Number)res[2]).intValue(), ((Number)res[3]).intValue()));
    }
}
