package bg.duosoft.ipas.persistence.repository.entity.ext;


import bg.duosoft.ipas.persistence.model.entity.ext.core.IpErrorLog;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import bg.duosoft.ipas.persistence.repository.entity.custom.IpErrorLogRepositoryCustom;
import org.springframework.data.jpa.repository.Query;

public interface IpErrorLogRepository extends BaseRepository<IpErrorLog, Integer>, IpErrorLogRepositoryCustom {

    @Query(value = "SELECT COUNT(*) FROM EXT_CORE.IP_ERROR_LOG e WHERE e.RESOLVED_DATE IS NULL", nativeQuery = true)
    Integer countAllUnresolved();

}
