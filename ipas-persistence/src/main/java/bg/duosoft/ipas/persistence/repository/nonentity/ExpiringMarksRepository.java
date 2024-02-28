package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.ExpiringMarkResult;
import bg.duosoft.ipas.persistence.model.nonentity.LastActionsResult;
import bg.duosoft.ipas.util.filter.LastActionFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExpiringMarksRepository {

    List<ExpiringMarkResult> selectExpiringMarks(Date expirationDateFrom, Date expirationDateTo);

}
