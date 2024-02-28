package bg.duosoft.ipas.persistence.repository.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.mark.IpMarkLogChanges;
import bg.duosoft.ipas.persistence.repository.entity.IpLogChangesRepositoryBase;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * User: ggeorgiev
 * Date: 30.1.2019 г.
 * Time: 11:51
 */
public interface IpMarkLogChangesRepository extends IpLogChangesRepositoryBase<IpMarkLogChanges> {
    @Query("select max(l.pk.logChangeNbr) from IpMarkLogChanges l where l.pk.fileSeq = ?1 and l.pk.fileTyp = ?2 and l.pk.fileSer = ?3 and l.pk.fileNbr = ?4")
    Optional<Integer> getMaxLogChangeNumber(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query("select a from IpMarkLogChanges a where a.pk.fileSeq = ?1 and a.pk.fileTyp = ?2 and a.pk.fileSer = ?3 and a.pk.fileNbr = ?4 order by a.changeDate")
    List<IpMarkLogChanges> getLogChanges(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

    @Query("select a from IpMarkLogChanges a where a.pk.fileSeq = ?1 and a.pk.fileTyp = ?2 and a.pk.fileSer = ?3 and a.pk.fileNbr = ?4 and a.pk.logChangeNbr = ?5")
    IpMarkLogChanges getLogChange(String fileSeq, String fileType, Integer fileSer, Integer fileNbr, Integer logChangeNbr);
}

