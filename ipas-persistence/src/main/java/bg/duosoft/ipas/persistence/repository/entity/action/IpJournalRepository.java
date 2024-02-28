package bg.duosoft.ipas.persistence.repository.entity.action;

import bg.duosoft.ipas.persistence.model.entity.action.IpJournal;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpJournalRepository extends BaseRepository<IpJournal, String> {

    @Query(value = "select * from IP_JOURNAL where ind_closed='N' order by jornal_name", nativeQuery = true)
    List<IpJournal> selectOpenJournals();


}
