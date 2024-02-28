package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocRequest;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.ReceptionUserdocRequestRepositoryCustom;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceptionUserdocRequestRepository extends BaseRepository<ReceptionUserdocRequest, Integer>, ReceptionUserdocRequestRepositoryCustom {

    @Query(value = "SELECT * FROM EXT_RECEPTION.RECEPTION_USERDOC_REQUEST r where r.ORIGINAL_EXPECTED = 1 " +
            " AND r.RELATED_OBJECT_SEQ = ?1 AND r.RELATED_OBJECT_TYP = ?2 AND r.RELATED_OBJECT_SER = ?3 AND r.RELATED_OBJECT_NBR = ?4 AND r.USERDOC_TYPE = ?5", nativeQuery = true)
    List<ReceptionUserdocRequest> selectOriginalExpectedUserdocs(String objectSeq, String objectTyp, Integer objectSer, Integer objectNumber, String userdocType);

    ReceptionUserdocRequest findByDocOriAndDocLogAndDocSerAndDocNbr(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Modifying
    @Query(value = "UPDATE ReceptionUserdocRequest r SET r.submissionType.id = ?1 WHERE r.docOri = ?2 AND r.docLog = ?3 AND r.docSer = ?4 AND r.docNbr = ?5")
    void updateSubmissionType(Integer submissionType, String docOri, String docLog, Integer docSer, Integer docNbr);

}
