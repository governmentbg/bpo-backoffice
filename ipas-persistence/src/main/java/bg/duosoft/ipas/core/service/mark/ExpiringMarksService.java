package bg.duosoft.ipas.core.service.mark;

import bg.duosoft.ipas.persistence.model.nonentity.ExpiringMarkResult;
import bg.duosoft.ipas.persistence.model.nonentity.LastActionsResult;
import bg.duosoft.ipas.util.filter.LastActionFilter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExpiringMarksService {

    List<ExpiringMarkResult> selectExpiringMarks(Date expirationDateFrom, Date expirationDateTo);

}
