package bg.duosoft.ipas.persistence.repository.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IpPatentRepository extends BaseRepository<IpPatent, IpFilePK> {

    List<IpPatent> findAllByPk_FileNbrAndPk_FileTypIn(Integer fileNbr, Collection<String> fileTypes);

    List<IpPatent> findAllByFile_RegistrationNbrAndPk_FileTypIn(Integer registrationNbr, Collection<String> fileTypes);

    @Query("select pt.expirationDate from IpPatent pt where pt.pk.fileSeq = ?1 and pt.pk.fileTyp = ?2 and pt.pk.fileSer = ?3 and pt.pk.fileNbr = ?4")
    Optional<Date> findPatentExpirationDate(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.IP_PATENT e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    Integer countById(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Modifying
    @Query(value = "UPDATE IpPatent d SET d.rowVersion = (d.rowVersion + 1) WHERE d.pk.fileSeq = ?1 AND d.pk.fileTyp = ?2 AND d.pk.fileSer = ?3 AND d.pk.fileNbr = ?4")
    void updateRowVersion(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Modifying
    @Query(value = "execute deletePatentDetails @fileSeq=?1, @fileTyp = ?2, @fileNbr=?3, @fileSer = ?4 ;", nativeQuery = true)
    void deletePatent(String fileSeq, String fileType, Integer fileNbr, Integer fileSer);

    @Query(value = "SELECT p.pk FROM IpPatent p")
    List<IpFilePK> listFilePK();
}
