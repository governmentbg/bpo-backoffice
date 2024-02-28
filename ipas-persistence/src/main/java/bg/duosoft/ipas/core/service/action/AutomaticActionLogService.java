package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.model.action.CAutomaticActionLogResult;
import bg.duosoft.ipas.util.filter.AutomaticActionsLogFilter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 05.12.2022
 * Time: 15:18
 */
public interface AutomaticActionLogService {
    public List<CAutomaticActionLogResult> getAutomaticActionLogs(AutomaticActionsLogFilter filter);
}
