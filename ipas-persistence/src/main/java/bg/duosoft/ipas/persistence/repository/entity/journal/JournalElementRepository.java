package bg.duosoft.ipas.persistence.repository.entity.journal;

import bg.duosoft.ipas.persistence.model.entity.journal.JournalElement;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.JournalElementRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JournalElementRepository extends BaseRepository<JournalElement, Integer>, JournalElementRepositoryCustom {
    @Query("SELECT j from JournalElement j WHERE j.procTyp = :procTyp and j.procNbr = :procNbr and j.actionNbr = :actionNbr")
    JournalElement selectByAction(@Param("procTyp") String procTyp, @Param("procNbr") Integer procNbr, @Param("actionNbr") Integer actionNbr);
}
