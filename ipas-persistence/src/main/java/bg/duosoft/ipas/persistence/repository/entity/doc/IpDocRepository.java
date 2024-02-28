package bg.duosoft.ipas.persistence.repository.entity.doc;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IpDocRepository extends BaseRepository<IpDoc, IpDocPK> {

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.IP_DOC e WHERE e.DOC_ORI = ?1 AND e.DOC_LOG = ?2 AND e.DOC_SER = ?3 AND e.DOC_NBR = ?4", nativeQuery = true)
    Integer countById(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query(value = "SELECT e.EXTERNAL_SYSTEM_ID FROM IPASPROD.IP_DOC e WHERE e.DOC_ORI = ?1 AND e.DOC_LOG = ?2 AND e.DOC_SER = ?3 AND e.DOC_NBR = ?4", nativeQuery = true)
    String selectExternalSystemId(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query(value = "SELECT DOC_SEQ_TYP FROM IPASPROD.IP_DOC e WHERE e.DOC_ORI = ?1 AND e.DOC_LOG = ?2 AND e.DOC_SER = ?3 AND e.DOC_NBR = ?4", nativeQuery = true)
    String selectDocumentSequenceType(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Modifying
    @Query(value = "UPDATE IpDoc d SET d.externalSystemId = ?1 WHERE d.pk.docOri = ?2 AND d.pk.docLog = ?3 AND d.pk.docSer = ?4 AND d.pk.docNbr = ?5")
    void updateExternalSystemId(String externalSystemId, String docOri, String docLog, Integer docSer, Integer docNbr);

    @Modifying
    @Query(value = "UPDATE IpDoc d SET d.indFaxReception = ?1 WHERE d.pk.docOri = ?2 AND d.pk.docLog = ?3 AND d.pk.docSer = ?4 AND d.pk.docNbr = ?5")
    void updateIndFaxRecetpion(String indFaxReception, String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query("SELECT DISTINCT new bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK (d.pk.docOri,d.pk.docLog,d.pk.docSer,d.pk.docNbr)" +
            " FROM IpDoc d where d.externalSystemId = ?1")
    List<IpDocPK> selectDocumentIdByExternalSystemId(String externalSystemId);

    @Query(value = "SELECT d.DOC_ORI, d.DOC_LOG, d.DOC_SER, d.DOC_NBR FROM IP_DOC d \n" +
            " JOIN IP_PROC p on d.DOC_ORI=p.DOC_ORI and d.DOC_NBR=p.DOC_NBR and d.DOC_LOG=p.DOC_LOG and d.DOC_SER=p.DOC_SER \n" +
            " WHERE d.EXTERNAL_SYSTEM_ID = :externalSystemId \n" +
            " AND p.USERDOC_TYP = :userdocTyp \n" +
            " AND p.USERDOC_FILE_SEQ = :fileSeq AND p.USERDOC_FILE_TYP = :fileTyp AND p.USERDOC_FILE_SER = :fileSer AND p.USERDOC_FILE_NBR = :fileNbr", nativeQuery = true)
    List<Object[]> selectDocumentIdByExternalSystemIdAndTypeAndParentFileId(@Param("externalSystemId") String externalSystemId, @Param("userdocTyp") String userdocTyp, @Param("fileSeq") String fileSeq, @Param("fileTyp") String fileTyp, @Param("fileSer") Integer fileSer, @Param("fileNbr") Integer fileNbr);

    @Modifying
    @Query(value = "UPDATE IpDoc d SET d.file.pk.fileSeq = ?1, d.file.pk.fileTyp = ?2, d.file.pk.fileSer = ?3, d.file.pk.fileNbr = ?4 where d.pk.docOri = ?5 and d.pk.docLog = ?6 and d.pk.docSer = ?7 and d.pk.docNbr = ?8")
    void updateFileKey(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr, String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query("SELECT DISTINCT new bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK (d.pk.docOri,d.pk.docLog,d.pk.docSer,d.pk.docNbr)" +
            " FROM IpDoc d where d.externalSystemId = ?1")
    List<IpDocPK> selectPkByExtarnalId(String externalSystemId);

    @Query("SELECT DISTINCT new bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK (d.pk.docOri,d.pk.docLog,d.pk.docSer,d.pk.docNbr)" +
            " FROM IpDoc d where d.pk.docOri = ?1 and d.pk.docLog = ?2 and d.docSeqSeries = ?3 and d.docSeqNbr = ?4")
    List<IpDocPK> selectPkByDocOriDocLogDocSeqSeriesDocSeqNbr(String docOri,String docLog,Integer docSeqSeries,Integer docSeqNbr);

    @Query(value = "SELECT d.pk FROM IpDoc d")
    List<IpDocPK> listDocPK();

}
