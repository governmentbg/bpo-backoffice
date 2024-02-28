package bg.duosoft.ipas.persistence.repository.entity.nomenclature;


import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLegalGroundTypes;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CfLegalGroundTypesRepository extends BaseRepository<CfLegalGroundTypes, Integer> {
    @Query(value="SELECT lgt.* FROM EXT_CORE.CF_LEGAL_GROUND_TYPES lgt" +
            " INNER JOIN EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL lgtp on lgt.id = lgtp.legal_ground_type_id" +
            " WHERE lgt.version = ?1 and lgtp.panel = ?2", nativeQuery = true)
    List<CfLegalGroundTypes> findAllLegalGroundsForSpecificPanel(String version, String panel);

    @Query(value="SELECT lgt.* FROM EXT_CORE.CF_LEGAL_GROUND_TYPES lgt" +
            " INNER JOIN EXT_CORE.CF_LEGAL_GROUND_TYPE_TO_UI_PANEL lgtp on lgt.id = lgtp.legal_ground_type_id" +
            " INNER JOIN EXT_CORE.CF_EARLIER_RIGHT_TO_LEGAL_GROUND lgte on lgt.id = lgte.legal_ground_type_id" +
            " WHERE lgt.version = ?1 and lgtp.panel = ?2 and lgte.earlier_right_type_id = ?3", nativeQuery = true)
    List<CfLegalGroundTypes> findAllLegalGroundsForSpecificPanelAndEarlierRight(String version,String panel,Integer earlierRightTypeId);

    @Query(value="SELECT lgt.* FROM EXT_CORE.CF_LEGAL_GROUND_TYPES lgt" +
            " WHERE lgt.version = ?1 and (legal_ground_code like '%'+?2 or legal_ground_code like '%'+?2+',%')", nativeQuery = true)
    List<CfLegalGroundTypes> findAllLegalGroundsByVersionAndCode(String version,String code);

}
