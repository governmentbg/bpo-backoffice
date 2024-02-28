package bg.duosoft.ipas.persistence.repository.entity.file;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRestrictData;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IpFileRepository extends BaseRepository<IpFile, IpFilePK> {

    @Query("SELECT DISTINCT new bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK (f.ipDoc.pk.docOri,f.ipDoc.pk.docLog,f.ipDoc.pk.docSer,f.ipDoc.pk.docNbr)" +
            " FROM IpFile f where f.pk.fileSeq = ?1 AND f.pk.fileTyp = ?2 AND f.pk.fileSer = ?3 AND f.pk.fileNbr = ?4")
    IpDocPK selectDocumentId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.IP_FILE e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    Integer countById(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query("SELECT new bg.duosoft.ipas.core.model.file.CFileId (c.pk.fileSeq,c.pk.fileTyp,c.pk.fileSer,c.pk.fileNbr) FROM IpFile c where c.pk.fileTyp = ?1")
    List<CFileId> selectObjetsByType(String fileType);

    @Query("SELECT new bg.duosoft.ipas.core.model.file.CFileRestrictData(f.applSubtyp,f.applTyp,f.pk.fileTyp,f.lawCode) FROM IpFile f where f.pk.fileSeq = ?1 AND f.pk.fileTyp = ?2 AND f.pk.fileSer = ?3 and f.pk.fileNbr = ?4")
    CFileRestrictData selectRestrictData(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT FILING_DATE FROM IPASPROD.IP_FILE e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    Date getFilingDate(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT e.APPL_TYP FROM IPASPROD.IP_FILE e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    String selectApplicationTypeByFileId(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT *\n" +
            "FROM IPASPROD.IP_FILE f\n" +
            "         LEFT JOIN IPASPROD.IP_MARK m\n" +
            "                   on f.FILE_NBR = m.FILE_NBR and f.FILE_SEQ = m.FILE_SEQ and f.FILE_TYP = m.FILE_TYP and\n" +
            "                      f.FILE_SER = m.FILE_SER\n" +
            "where m.FILE_NBR is null AND f.FILE_NBR = :fileNumber AND f.FILE_TYP IN :fileTypes", nativeQuery = true)
    List<IpFile> selectMarkReceptionFiles(@Param("fileTypes") List<String> fileTypes, @Param("fileNumber") Integer fileNumber);

    @Query(value = "SELECT *\n" +
            "FROM IPASPROD.IP_FILE f\n" +
            "         LEFT JOIN IPASPROD.IP_PATENT m\n" +
            "                   on f.FILE_NBR = m.FILE_NBR and f.FILE_SEQ = m.FILE_SEQ and f.FILE_TYP = m.FILE_TYP and\n" +
            "                      f.FILE_SER = m.FILE_SER\n" +
            "where m.FILE_NBR is null AND f.FILE_NBR = :fileNumber AND f.FILE_TYP IN :fileTypes", nativeQuery = true)
    List<IpFile> selectPatentReceptionFiles(@Param("fileTypes") List<String> fileTypes, @Param("fileNumber") Integer fileNumber);

    @Query(value = "SELECT e.REGISTRATION_NBR FROM IPASPROD.IP_FILE e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    Integer selectRegistrationNumberById(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT e FROM IpFile e WHERE e.registrationNbr = :registrationNbr AND ((:registrationDup is null and (e.registrationDup is null or e.registrationDup='')) or e.registrationDup = :registrationDup) AND e.pk.fileTyp in (:fileTypes)")
    List<IpFile> findAllByRegistrationNbrAndDupAndFileType(@Param("registrationNbr") Integer registrationNbr, @Param("registrationDup") String registrationDup, @Param("fileTypes") List<String> fileTypes);

    @Query(value = "SELECT e FROM IpFile e WHERE e.registrationNbr = :registrationNbr AND e.pk.fileTyp in (:fileTypes)")
    List<IpFile> findAllByRegistrationNbrAndFileType(@Param("registrationNbr") Integer registrationNbr, @Param("fileTypes") List<String> fileTypes);

    @Query(value = "SELECT e FROM IpFile e WHERE e.pk.fileNbr = :fileNbr AND e.pk.fileTyp in (:fileTypes)")
    List<IpFile> findAllByFileNbrAndFileType(@Param("fileNbr") Integer fileNbr, @Param("fileTypes") List<String> fileTypes);


}
