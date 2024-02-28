package bg.duosoft.ipas.persistence.repository.entity.reception;

import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocCorrespondent;
import bg.duosoft.ipas.persistence.model.entity.ext.reception.ReceptionUserdocCorrespondentPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceptionUserdocCorrespondentRepository extends BaseRepository<ReceptionUserdocCorrespondent, ReceptionUserdocCorrespondentPK> {

    @Query(value = "SELECT * FROM EXT_RECEPTION.USERDOC_CORRESPONDENT c where c.RECEPTION_USERDOC_REQUEST_ID = ?1 AND c.TYPE = ?2", nativeQuery = true)
    List<ReceptionUserdocCorrespondent> selectReceptionCorrespondentByIdAndType(Integer id, Integer type);


}
