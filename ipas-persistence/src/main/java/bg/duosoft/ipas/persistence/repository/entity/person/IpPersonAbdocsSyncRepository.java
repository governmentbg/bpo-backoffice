package bg.duosoft.ipas.persistence.repository.entity.person;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpPersonAbdocsSync;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPersonAbdocsSyncRepository extends BaseRepository<IpPersonAbdocsSync, Integer> {

    @Query(value = "SELECT * FROM EXT_CORE.IP_PERSON_ABDOCS_SYNC p WHERE p.PROCESSED_AT IS NULL ORDER BY p.INSERTED_AT ", nativeQuery = true)
    List<IpPersonAbdocsSync> selectNotProcessed();

}
