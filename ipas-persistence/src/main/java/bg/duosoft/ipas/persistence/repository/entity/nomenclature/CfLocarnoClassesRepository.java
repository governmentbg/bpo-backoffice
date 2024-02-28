package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassLocarno;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassLocarnoPK;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CfLocarnoClassesRepository extends BaseRepository<CfClassLocarno, CfClassLocarnoPK> {
    @Query("SELECT lc FROM  CfClassLocarno lc where lc.pk.locarnoClassCode like :locarnoClassCode")
    List<CfClassLocarno> findByLocarnoClassCode(@Param("locarnoClassCode")String locarnoClassCode ,Pageable pageable);

    @Query("SELECT lc FROM  CfClassLocarno lc where lc.pk.locarnoClassCode = :locarnoClassCode")
    List<CfClassLocarno> findByLocarnoClassCode(@Param("locarnoClassCode")String locarnoClassCode );

    @Query(value = "SELECT COUNT(*) FROM IPASPROD.CF_CLASS_LOCARNO e WHERE LOCARNO_CLASS_CODE=?1", nativeQuery = true)
    Integer countByClassCode(@Param("locarnoClassCode")String locarnoClassCode );
}
