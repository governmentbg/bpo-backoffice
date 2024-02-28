package bg.duosoft.ipas.persistence.repository.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
public interface IpMarkRepository extends BaseRepository<IpMark, IpFilePK> {


    List<IpMark> findAllByPk_FileNbrAndPk_FileTypIn(Integer fileNbr, Collection<String> fileTypes);

    @Query(value = "select * from ipasprod.ip_mark where (file_typ in ('I', 'R', 'B') and REGISTRATION_NBR=?1) or (file_typ in ('N', 'D') and INTREGN like ?2)", nativeQuery = true)
    List<IpMark> findMarkOnSearchTypeRegNumberAndFileTypeInternationalMark(Integer registrationNbr,String inregn);

    List<IpMark> findAllByFile_RegistrationNbrAndPk_FileTypIn(Integer registrationNbr, Collection<String> fileTypes);

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.IP_MARK e WHERE e.FILE_SEQ = ?1 AND e.FILE_TYP = ?2 AND e.FILE_SER = ?3 AND e.FILE_NBR = ?4", nativeQuery = true)
    Integer countById(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Modifying
    @Query(value = "UPDATE IpMark d SET d.rowVersion = (d.rowVersion + 1) WHERE d.pk.fileSeq = ?1 AND d.pk.fileTyp = ?2 AND d.pk.fileSer = ?3 AND d.pk.fileNbr = ?4")
    void updateRowVersion(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT m.pk FROM IpMark m")
    List<IpFilePK> listFilePK();

    @Query(value = "SELECT concat(m.FILE_SEQ,'/', m.FILE_TYP,'/', m.FILE_SER,'/', m.FILE_NBR) FROM IPASPROD.IP_MARK m WHERE m.FILE_TYP in ('I', 'R') and m.FILE_SER >= 2020", nativeQuery = true)
    List<String> selectInternationalMarkIds();

    @Modifying
    @Query(value = "exec deleteMarkDetails @fileSeq=?1, @fileTyp = ?2, @fileSer=?3, @fileNbr = ?4", nativeQuery = true)
    void deleteMark(String fileSeq, String fileTyp, Integer fileSer, Integer fileNbr);
}
