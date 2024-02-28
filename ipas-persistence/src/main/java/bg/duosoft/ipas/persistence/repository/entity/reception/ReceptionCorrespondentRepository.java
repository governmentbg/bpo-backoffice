package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionCorrespondent;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionCorrespondentPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionCorrespondentRepository extends BaseRepository<ReceptionCorrespondent, ReceptionCorrespondentPK> {

    @Query(value = "SELECT c.* " +
            "FROM EXT_RECEPTION.CORRESPONDENT c " +
            "JOIN EXT_RECEPTION.RECEPTION_REQUEST r on c.RECEPTION_REQUEST_ID = r.ID " +
            "where r.FILE_SEQ = ?1 AND r.FILE_TYP = ?2 AND r.FILE_SER = ?3 AND r.FILE_NBR = ?4", nativeQuery = true)
    List<ReceptionCorrespondent> findReceptionPersons(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query(value = "SELECT * FROM EXT_RECEPTION.CORRESPONDENT c where c.RECEPTION_REQUEST_ID = ?1 AND c.TYPE = ?2", nativeQuery = true)
    List<ReceptionCorrespondent> selectReceptionCorrespondentByIdAndType(Integer id, Integer type);

}
