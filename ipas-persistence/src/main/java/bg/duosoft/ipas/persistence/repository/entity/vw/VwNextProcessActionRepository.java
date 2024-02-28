package bg.duosoft.ipas.persistence.repository.entity.vw;

import bg.duosoft.ipas.persistence.model.entity.vw.VwSelectNextProcessActions;
import bg.duosoft.ipas.persistence.model.entity.vw.VwSelectNextProcessActionsId;
import bg.duosoft.ipas.persistence.repository.entity.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwNextProcessActionRepository extends BaseRepository<VwSelectNextProcessActions, VwSelectNextProcessActionsId> {

    @Query(value = "SELECT * FROM VW_SELECT_NEXT_PROCESS_ACTIONS v WHERE v.PROC_TYP = ?1 AND (v.INITIAL_STATUS_CODE = ?2 OR v.INITIAL_STATUS_CODE = '') ORDER BY v.ACTION_TYPE_NAME", nativeQuery = true)
    List<VwSelectNextProcessActions> selectNextProcessActions(String procType, String initialStatusCode);

    @Query(value = "SELECT * FROM VW_SELECT_NEXT_PROCESS_ACTIONS v WHERE v.PROC_TYP = ?1 AND (v.INITIAL_STATUS_CODE = ?2 OR v.INITIAL_STATUS_CODE = '') AND v.AUTOMATIC_ACTION_WCODE = 1 AND v.PROCESS_ACTION_TYPE = 'NORMAL' ORDER BY v.ACTION_TYPE_NAME", nativeQuery = true)
    List<VwSelectNextProcessActions> selectNextProcessActionsAfterExpirationDate(String procType, String initialStatusCode);

    @Query(value = "SELECT * FROM VW_SELECT_NEXT_PROCESS_ACTIONS v WHERE v.PROC_TYP = ?1 AND v.STATUS_CODE = ?2 AND (v.DUE_DATE_CALCULATION_WCODE is not null or v.AUTOMATIC_ACTION_WCODE = 2) AND v.PROCESS_ACTION_TYPE = 'NORMAL' ORDER BY v.ACTION_TYPE_NAME", nativeQuery = true)
    List<VwSelectNextProcessActions> selectActionsWithExpirationDate(String procType, String statusCode);

}
