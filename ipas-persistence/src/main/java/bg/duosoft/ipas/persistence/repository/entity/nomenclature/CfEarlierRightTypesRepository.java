package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfEarlierRightTypes;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  CfEarlierRightTypesRepository extends BaseRepository<CfEarlierRightTypes, Integer> {

    @Query(value="SELECT earl.* FROM EXT_CORE.CF_EARLIER_RIGHT_TYPES earl" +
            " INNER JOIN EXT_CORE.CF_EARLIER_RIGHT_TO_PANEL earlto on earl.id = earlto.earlier_right_type_id" +
            " WHERE earlto.panel = ?1 and earl.version = ?2", nativeQuery = true)
    List<CfEarlierRightTypes> findAllEarlierRightTypesForSpecificPanel(String panel,String version);

    @Query(value="SELECT earl.* FROM EXT_CORE.CF_EARLIER_RIGHT_TYPES earl" +
            " WHERE earl.version = ?1 and earl.earlier_right_type_code like '%'+ ?2+ '%'", nativeQuery = true)
    CfEarlierRightTypes findTypeByVersionAndCode(String version,String code);
}
