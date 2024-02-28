package bg.duosoft.ipas.persistence.repository.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPerson;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocPersonPK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpUserdocPersonRepository extends BaseRepository<IpUserdocPerson, IpUserdocPersonPK> {


    @Query(value = "SELECT udp.pk FROM IpUserdocPerson udp")
    List<IpUserdocPersonPK> listUserdocPersonPK();

    @Query(value = "SELECT COUNT(*) FROM EXT_CORE.IP_USERDOC_PERSON e WHERE e.DOC_ORI = ?1 AND e.DOC_LOG = ?2 AND e.DOC_SER = ?3 AND e.DOC_NBR = ?4 AND e.ROLE = ?5", nativeQuery = true)
    int countByRole(String docOri, String docLog, Integer docSer, Integer docNbr, String role);
}
