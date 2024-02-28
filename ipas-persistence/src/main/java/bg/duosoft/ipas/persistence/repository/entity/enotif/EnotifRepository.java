package bg.duosoft.ipas.persistence.repository.entity.enotif;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordal;
import bg.duosoft.ipas.persistence.model.entity.mark.Enotif;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnotifRepository extends BaseRepository<Enotif, String> {
    @Query(value = "SELECT * FROM EXT_CORE.ENOTIF ORDER BY GAZNO DESC ", nativeQuery = true)
    List<Enotif> findAllOrOrderByGaznoDesc();

    @Query(value = "SELECT count(*) FROM EXT_CORE.ENOTIF", nativeQuery = true)
    Integer getEnotifsCount();

    @Query(value = "SELECT top 5 * FROM EXT_CORE.ENOTIF ORDER BY GAZNO DESC ", nativeQuery = true)
    List<Enotif> findAllWithTopOrderByGaznoDesc();
}
