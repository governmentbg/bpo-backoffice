package bg.duosoft.ipas.persistence.repository.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfActionType;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CfActionTypeRepository extends BaseRepository<CfActionType, String> {

    @Query(value = " SELECT a.* FROM CF_ACTION_TYPE a " +
            " JOIN CF_MIGRATION m ON m.ACTION_TYP = a.ACTION_TYP " +
            " WHERE m.PROC_TYP IN ?1 AND a.ACTION_TYPE_NAME LIKE ?2 " +
            " ORDER BY a.ACTION_TYPE_NAME ", nativeQuery = true)
    List<CfActionType> findAllByProcTypesAndActionTypeNameContainsOrderByActionTypeName(List<String> procTypes, String actionName);

    @Query(value = " SELECT a.* FROM CF_ACTION_TYPE a " +
            "  JOIN CF_MIGRATION m ON m.ACTION_TYP = a.ACTION_TYP " +
            "  JOIN [IPASPROD].[CF_APPLICATION_TYPE] apt ON m.PROC_TYP = apt.GENERATE_PROC_TYP " +
            "  WHERE apt.FILE_TYP IN ?1 AND a.ACTION_TYPE_NAME LIKE ?2 " +
            "  ORDER BY a.ACTION_TYPE_NAME ", nativeQuery = true)
    List<CfActionType> findAllByActionTypeNameContainsAndFileTypesOrderByActionTypeName(List<String> procTypes, String actionName);

    @Query(value = " SELECT a.* FROM CF_ACTION_TYPE a WHERE a.AUTOMATIC_ACTION_WCODE = ?1", nativeQuery = true)
    List<CfActionType> findAllByAutomaticActionWcode(Integer automaticWCode);

    List<CfActionType> findAllByActionTypInOrderByActionTypeName(Collection<String> actionTyp);

}
