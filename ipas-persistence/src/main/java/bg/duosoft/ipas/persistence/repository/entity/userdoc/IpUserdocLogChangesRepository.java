package bg.duosoft.ipas.persistence.repository.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLogChanges;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocLogChanges;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdocLogChangesPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * User: ggeorgiev
 * Date: 22.11.2021
 * Time: 14:53
 */
public interface IpUserdocLogChangesRepository extends BaseRepository<IpUserdocLogChanges, IpUserdocLogChangesPK> {
    @Query("select max(l.pk.logChangeNbr) from IpUserdocLogChanges l where l.pk.docOri = ?1 and l.pk.docLog = ?2 and l.pk.docSer = ?3 and l.pk.docNbr = ?4")
    Optional<Integer> getMaxLogChangeNumber(String docOri, String docLog, Integer docSer, Integer docNbr);


    @Query("select a from IpUserdocLogChanges a where a.pk.docOri = ?1 and a.pk.docLog = ?2 and a.pk.docSer = ?3 and a.pk.docNbr = ?4 order by a.changeDate")
    List<IpUserdocLogChanges> getLogChanges(String docOri, String docLog, Integer docSer, Integer docNbr);

    @Query("select a from IpUserdocLogChanges a where a.pk.docOri = ?1 and a.pk.docLog = ?2 and a.pk.docSer = ?3 and a.pk.docNbr = ?4 and a.pk.logChangeNbr = ?5")
    IpUserdocLogChanges getLogChange(String docOri, String docLog, Integer docSer, Integer docNbr, Integer logChangeNbr);
}
