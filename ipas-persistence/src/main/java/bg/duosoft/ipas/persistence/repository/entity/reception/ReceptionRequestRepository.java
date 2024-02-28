package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionRequest;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceptionRequestRepository extends BaseRepository<ReceptionRequest, Integer> {
    @Modifying
    @Query(value = "UPDATE ReceptionRequest r SET r.status = 1 WHERE r.fileSeq = ?1 AND r.fileType = ?2 AND r.fileSer = ?3 AND r.fileNbr = ?4")
    void updateReceptionRequestStatus(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    ReceptionRequest findReceptionRequestByFileSeqAndFileTypeAndFileSerAndFileNbr(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT * FROM EXT_RECEPTION.RECEPTION_REQUEST r where r.ORIGINAL_EXPECTED = 1 AND r.NAME = ?1 AND FILE_TYP = ?2", nativeQuery = true)
    List<ReceptionRequest> selectOriginalExpectedByNameAndFileType(String name, String fileType);


    @Modifying
    @Query(value = "UPDATE ReceptionRequest r SET r.submissionType.id = ?1 WHERE r.fileSeq = ?2 AND r.fileType = ?3 AND r.fileSer = ?4 AND r.fileNbr = ?5")
    void updateSubmissionType(Integer submissionType, String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

}
