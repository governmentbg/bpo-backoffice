package bg.duosoft.ipas.persistence.repository.entity.process;

import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpProcRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IpProcRepository extends BaseRepository<IpProc, IpProcPK>, IpProcRepositoryCustom {

    List<IpProc> findAllByUpperProc_Pk(IpProcPK upperProcPk);

    IpProc findByUserdocIpDoc_Pk_DocOriAndUserdocIpDoc_Pk_DocLogAndUserdocIpDoc_Pk_DocSerAndUserdocIpDoc_Pk_DocNbr(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query(value = "SELECT\n" +
            "       CASE\n" +
            "           WHEN up.DOC_ORI IS NOT NULL AND up.DOC_LOG IS NOT NULL AND up.DOC_SER IS NOT NULL AND up.DOC_NBR IS NOT NULL THEN 'true'\n" +
            "           ELSE 'false'\n" +
            "        END AS result " +
            "FROM IP_PROC up\n" +
            "where up.PROC_TYP = (SELECT p.UPPER_PROC_TYP FROM IP_PROC p where p.DOC_ORI = ?1 AND p.DOC_LOG = ?2 and p.DOC_SER = ?3 and p.DOC_NBR = ?4)\n" +
            "and  up.PROC_NBR  = (SELECT p.UPPER_PROC_NBR FROM IP_PROC p where p.DOC_ORI = ?1 AND p.DOC_LOG = ?2 and p.DOC_SER = ?3 and p.DOC_NBR = ?4)\n", nativeQuery = true)
    Boolean isUpperProcessUserdoc(String docOri, String docLog, Integer docSer, Integer docNbr);


    @Query(value = "select RESPONSIBLE_USER_ID from IP_PROC where UPPER_PROC_TYP = ? and UPPER_PROC_NBR = ? and RESPONSIBLE_USER_ID is not null", nativeQuery = true)
    List<BigDecimal> selectSubProcessesResponsibleUserIds(String procTyp, Integer procNbr);


    @Modifying
    @Query(value = "UPDATE IpProc  p SET p.userdocTyp.userdocTyp = ?1 WHERE p.pk.procTyp = ?2 AND p.pk.procNbr = ?3")
    void updateUserdocType(String userdocType, String procType, Integer procNbr);

    @Modifying
    @Query(value = "UPDATE IpProc  p SET p.responsibleUser.userId = ?1 WHERE p.pk.procTyp = ?2 AND p.pk.procNbr = ?3")
    void updateResponsibleUser(Integer userId, String processType, Integer processNbr);

    @Modifying
    @Query(value = "UPDATE IP_PROC  SET STATUS_CODE = ?1 , STATUS_DATE = GETDATE() WHERE PROC_TYP = ?2 AND PROC_NBR = ?3",nativeQuery = true)
    void updateStatusCodeAndDateById(String statusCode,String processType, Integer processNbr);

    @Query(value = "SELECT CONCAT(op.PROC_TYP, '-', op.PROC_NBR)\n" +
            "FROM IP_PROC op\n" +
            "where op.UPPER_PROC_TYP = ?1\n" +
            "  and op.UPPER_PROC_NBR = ?2\n" +
            "  and ((op.DOC_ORI IS NOT NULL AND op.DOC_LOG IS NOT NULL AND op.DOC_SER IS NOT NULL AND op.DOC_NBR IS NOT NULL) OR\n" +
            "       (op.OFFIDOC_ORI IS NOT NULL AND op.OFFIDOC_SER IS NOT NULL AND op.OFFIDOC_NBR IS NOT NULL))\n" +
            "\n" +
            "  AND ((\n" +
            "           SELECT COUNT(*)\n" +
            "           FROM IP_PROC p\n" +
            "           where p.UPPER_PROC_TYP = op.PROC_TYP\n" +
            "             and p.UPPER_PROC_NBR = op.PROC_NBR\n" +
            "             and ((p.DOC_ORI IS NOT NULL AND p.DOC_LOG IS NOT NULL AND p.DOC_SER IS NOT NULL AND\n" +
            "                   p.DOC_NBR IS NOT NULL) OR\n" +
            "                  (p.OFFIDOC_ORI IS NOT NULL AND p.OFFIDOC_SER IS NOT NULL AND p.OFFIDOC_NBR IS NOT NULL))\n" +
            "       ) > 0)", nativeQuery = true)
    List<String> selectProcessIdsOfUserdocAndOffidocChildren(String procTyp, Integer procNbr);

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.IP_PROC p WHERE p.UPPER_PROC_TYP = ?1 AND p.UPPER_PROC_NBR = ?2 AND p.USERDOC_TYP IS NOT NULL AND p.DOC_NBR IS NOT NULL", nativeQuery = true)
    int selectUserdocUpperProcessesCount(String procTyp, Integer procNbr);

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.IP_PROC p WHERE p.UPPER_PROC_TYP = ?1 AND p.UPPER_PROC_NBR = ?2", nativeQuery = true)
    int selectUpperProcessesCount(String procTyp, Integer procNbr);


    @Query(value = "SELECT COUNT(*)\n" +
            "FROM IPASPROD.IP_PROC p\n" +
            "         JOIN IPASPROD.IP_PROC tp on tp.PROC_TYP = p.UPPER_PROC_TYP and tp.PROC_NBR = p.UPPER_PROC_NBR\n" +
            "JOIN IPASPROD.CF_STATUS tps on tp.PROC_TYP = tps.PROC_TYP and tp.STATUS_CODE = tps.STATUS_CODE AND tps.PROCESS_RESULT_TYP = 'R'\n" +
            "where p.DOC_ORI = ?1\n" +
            "  AND p.DOC_LOG = ?2\n" +
            "  AND p.DOC_SER = ?3\n" +
            "  AND p.DOC_NBR = ?4", nativeQuery = true)
    Integer isUpperProcessOfUserdocIsInStatusRegistered(String docOrigin, String docLog, Integer docSeries, Integer docNbr);

    @Query(value = "SELECT p.RESPONSIBLE_USER_ID FROM IPASPROD.IP_PROC p WHERE p.PROC_TYP = ?1 AND p.PROC_NBR = ?2", nativeQuery = true)
    Integer selectResponsibleUser(String procTyp, Integer procNbr);

    @Query(value = "SELECT ud.pk FROM IpProc ud")
    List<IpProcPK> listIpProcPK();

    @Modifying
    @Query(value = "UPDATE IpProc d SET d.creationDate = ?1 WHERE d.userdocIpDoc.pk.docOri = ?2 AND d.userdocIpDoc.pk.docLog = ?3 AND d.userdocIpDoc.pk.docSer = ?4 AND d.userdocIpDoc.pk.docNbr = ?5")
    void updateUserdocProcessCreationDate(Date creationDate, String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query(value = "SELECT p.USERDOC_TYP FROM IPASPROD.IP_PROC p WHERE p.PROC_TYP = ?1 AND p.PROC_NBR = ?2", nativeQuery = true)
    String selectUserdocTypeByProcessId(String procTyp, Integer procNbr);


    @Query(value = "SELECT p FROM IpProc p where p.userdocTyp is not null and p.upperProc.pk.procTyp = ?1 and p.upperProc.pk.procNbr = ?2")
    List<IpProc> selectSubUserdocProcessesRelatedToIpObjectProcess(String procTyp, Integer procNbr);

    @Query(value = "SELECT d.EXTERNAL_SYSTEM_ID, d.FILING_DATE, s.STATUS_NAME\n" +
            "FROM IPASPROD.IP_PROC p\n" +
            "         JOIN IPASPROD.IP_DOC d\n" +
            "              on p.DOC_ORI = d.DOC_ORI and p.DOC_LOG = d.DOC_LOG and p.DOC_SER = d.DOC_SER and p.DOC_NBR = d.DOC_NBR\n" +
            "         JOIN IPASPROD.CF_STATUS s on p.STATUS_CODE = s.STATUS_CODE\n" +
            "where p.UPPER_PROC_TYP = ?1\n" +
            "  AND p.UPPER_PROC_NBR = ?2\n" +
            "  AND p.USERDOC_TYP = ?3", nativeQuery = true)
    List<Object[]> selectSubUserdocPartialDataByUserdocTyp(String procTyp, Integer procNbr, String userdocTyp);

}
