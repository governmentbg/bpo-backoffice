package bg.duosoft.ipas.persistence.repository.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypes;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocTypesPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface IpUserdocTypesRepository extends BaseRepository<IpUserdocTypes, IpUserdocTypesPK> {

    @Query(value = "SELECT * FROM IP_USERDOC_TYPES p where p.DOC_ORI = ?1 AND p.DOC_LOG = ?2 and p.DOC_SER = ?3 and p.DOC_NBR = ?4", nativeQuery = true)
    IpUserdocTypes selectByDocId(String docOri, String docLog, Integer docSer, Integer docNbr);
}
