package bg.duosoft.ipas.persistence.repository.entity.custom.impl;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocHierarchyChildNode;
import bg.duosoft.ipas.persistence.model.nonentity.ProcessEventResult;
import bg.duosoft.ipas.persistence.model.nonentity.TopProcessFileData;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpProcRepositoryCustom;
import bg.duosoft.ipas.util.process.ProcessEventUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Transactional
public class IpProcRepositoryImpl extends BaseRepositoryCustomImpl implements IpProcRepositoryCustom {

    @Override
    public IpFilePK selectTopProcessFileId(String processType, Integer processNbr) {
        Query q = em.createNativeQuery("SELECT tp.FILE_SEQ as fileSeq, tp.FILE_TYP as fileTyp, tp.FILE_SER as fileSer, tp.FILE_NBR as fileNbr \n" +
                "FROM IP_PROC p JOIN IP_PROC tp on p.FILE_PROC_TYP = tp.PROC_TYP and p.FILE_PROC_NBR = tp.PROC_NBR\n" +
                "where p.PROC_NBR = :processNbr and p.PROC_TYP = :processType");

        q.setParameter("processType", processType);
        q.setParameter("processNbr", processNbr);

        List<Object[]> resultList = q.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Object[] result = resultList.get(0);
        return new IpFilePK((String) result[0], (String) result[1], ((BigDecimal) result[2]).intValue(), ((BigDecimal) result[3]).intValue());
    }

    @Override
    public IpProcPK selectUserdocProcessId(String docOri, String docLog, Integer docSer, Integer docNbr) {
        Query q = em.createNativeQuery("SELECT p.PROC_TYP, p.PROC_NBR FROM IPASPROD.IP_PROC p where p.DOC_ORI = :docOri  AND p.DOC_LOG = :docLog AND p.DOC_SER = :docSer AND p.DOC_NBR = :docNbr");
        setDocumentIdParams(docOri, docLog, docSer, docNbr, q);

        return getProcPk(q);
    }

    public IpProcPK selectFileProcessId(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr) {
        Query q = em.createNativeQuery("SELECT p.PROC_TYP, p.PROC_NBR FROM IPASPROD.IP_PROC p where p.FILE_SEQ = :fileSeq  AND p.FILE_TYP = :fileTyp AND p.FILE_SER = :fileSer AND p.FILE_NBR = :fileNbr");
        setFileIdParams(fileSeq, fileTyp, fileSer, fileNbr, q);

        return getProcPk(q);
    }

    private IpProcPK getProcPk(Query q) {
        List<Object[]> resultList = q.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Object[] result = resultList.get(0);
        return new IpProcPK((String) result[0], ((BigDecimal) result[1]).intValue());
    }

    @Override
    public Integer selectResponsibleUserOfUserdocParentProcess(String docOri, String docLog, Integer docSer, Integer docNbr) {
        Query query = em.createNativeQuery("SELECT tp.RESPONSIBLE_USER_ID\n" +
                "FROM IPASPROD.IP_PROC p\n" +
                "         JOIN IP_PROC tp on tp.PROC_TYP = p.UPPER_PROC_TYP and tp.PROC_NBR = p.UPPER_PROC_NBR\n" +
                "where p.DOC_ORI = :docOri\n" +
                "  AND p.DOC_LOG = :docLog\n" +
                "  AND p.DOC_SER = :docSer\n" +
                "  AND p.DOC_NBR = :docNbr");
        setDocumentIdParams(docOri, docLog, docSer, docNbr, query);

        List<BigDecimal> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        BigDecimal result = resultList.get(0);
        return Objects.isNull(result) ? null : result.intValue();
    }

    @Override
    public Integer selectIpObjectResponsibleUser(CFileId fileId) {
        Query query = em.createNativeQuery("SELECT p.RESPONSIBLE_USER_ID " +
                "FROM IPASPROD.IP_PROC p " +
                " WHERE p.FILE_SEQ = :fileSeq " +
                "  AND p.FILE_TYP = :fileTyp " +
                "  AND p.FILE_SER = :fileSer " +
                "  AND p.FILE_NBR = :fileNbr");

        setFileIdParams(fileId.getFileSeq(), fileId.getFileType(), fileId.getFileSeries(), fileId.getFileNbr(), query);
        List<BigDecimal> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        BigDecimal result = resultList.get(0);
        return Objects.isNull(result) ? null : result.intValue();
    }

    @Override
    public Integer selectUserdocResponsibleUser(CDocumentId documentId) {
        Query query = em.createNativeQuery("SELECT p.RESPONSIBLE_USER_ID\n" +
                "FROM IPASPROD.IP_PROC p\n" +
                "where p.DOC_ORI = :docOri\n" +
                "  AND p.DOC_LOG = :docLog\n" +
                "  AND p.DOC_SER = :docSer\n" +
                "  AND p.DOC_NBR = :docNbr");
        setDocumentIdParams(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr(), query);

        List<BigDecimal> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        BigDecimal result = resultList.get(0);
        return Objects.isNull(result) ? null : result.intValue();
    }

    @Override
    public Integer selectOffidocResponsibleUser(COffidocId offidocId) {
        Query query = em.createNativeQuery("SELECT p.RESPONSIBLE_USER_ID\n" +
                "FROM IPASPROD.IP_PROC p\n" +
                "where p.OFFIDOC_ORI = :offidocOri\n" +
                "  AND p.OFFIDOC_SER = :offidocSer\n" +
                "  AND p.OFFIDOC_NBR = :offidocNbr");
        setOffidocIdParams(offidocId.getOffidocOrigin(), offidocId.getOffidocSeries(), offidocId.getOffidocNbr(), query);

        List<BigDecimal> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        BigDecimal result = resultList.get(0);
        return Objects.isNull(result) ? null : result.intValue();
    }

    @Override
    public List<ProcessEventResult> selectByUpperProcess(String procTyp, Integer procNbr) {
        Query query = em.createNativeQuery("SELECT p.PROC_TYP, p.PROC_NBR, p.UPPER_PROC_TYP, p.UPPER_PROC_NBR,\n" +
                "       p.FILE_SEQ, p.FILE_TYP, p.FILE_SER, p.FILE_NBR,\n" +
                "       p.DOC_ORI, p.DOC_LOG, p.DOC_SER, p.DOC_NBR,\n" +
                "       p.OFFIDOC_ORI, p.OFFIDOC_SER, p.OFFIDOC_NBR,\n" +
                "       p.CREATION_DATE, p.EXPIRATION_DATE, p.STATUS_CODE,\n" +
                "       p.USERDOC_TYP, d.FILING_DATE,\n" +
                "       d.DOC_SEQ_SERIES, d.DOC_SEQ_TYP, d.DOC_SEQ_NBR,cdf. DOC_SEQ_NAME,\n" +
                "       d.EXTERNAL_SYSTEM_ID, per.PERSON_NAME, u.NOTES, iu.USER_NAME\n" +
                "FROM IPASPROD.IP_PROC p\n" +
                "         LEFT JOIN IPASPROD.IP_DOC d on p.DOC_ORI = d.DOC_ORI AND p.DOC_LOG = d.DOC_LOG AND p.DOC_SER = d.DOC_SER AND\n" +
                "                               p.DOC_NBR = d.DOC_NBR\n" +
                "         LEFT JOIN IPASPROD.IP_USERDOC u on d.DOC_NBR = u.DOC_NBR and d.DOC_ORI = u.DOC_ORI and d.DOC_LOG = u.DOC_LOG and\n" +
                "                                   d.DOC_SER = u.DOC_SER\n" +
                "         LEFT JOIN IPASPROD.IP_PERSON per on per.PERSON_NBR = u.APPLICANT_PERSON_NBR\n" +
                "         LEFT JOIN IPASPROD.CF_DOC_SEQUENCE cdf on d.DOC_SEQ_TYP = cdf.DOC_SEQ_TYP\n" +
                "         LEFT JOIN IPASPROD.IP_USER iu on p.RESPONSIBLE_USER_ID = iu.USER_ID\n" +
                "where p.UPPER_PROC_TYP = :processTyp\n" +
                "  and p.UPPER_PROC_NBR = :processNbr");
        setProcessParams(procTyp, procNbr, query);
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        return resultList.stream()
                .map(ProcessEventUtils::convertToProcessEventResult)
                .collect(Collectors.toList());
    }

    @Override
    public TopProcessFileData selectTopProcessFileData(CProcessId topProcessId) {
        Query query = em.createNativeQuery("SELECT p.FILE_SEQ, p.FILE_TYP, p.FILE_SER, p.FILE_NBR, " +
                "       f.TITLE, s.STATUS_CODE, s.STATUS_NAME, f.REGISTRATION_NBR " +
                "FROM IPASPROD.IP_PROC p " +
                "         JOIN IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE " +
                "         JOIN IPASPROD.IP_FILE f on p.FILE_SEQ = f.FILE_SEQ AND p.FILE_TYP = f.FILE_TYP AND p.FILE_SER = f.FILE_SER AND\n" +
                "                                    P.FILE_NBR = f.FILE_NBR " +
                "where p.PROC_NBR = :processNbr " +
                "  and p.PROC_TYP = :processTyp");
        setProcessParams(topProcessId.getProcessType(), topProcessId.getProcessNbr(), query);
        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        Object[] result = resultList.get(0);

        return new TopProcessFileData(topProcessId,
                new CFileId((String) result[0], (String) result[1], ((BigDecimal) result[2]).intValue(), ((BigDecimal) result[3]).intValue()),
                (String) result[4], (String) result[5], (String) result[6], result[7] == null ? null : ((BigDecimal) result[7]).intValue());
    }



}
