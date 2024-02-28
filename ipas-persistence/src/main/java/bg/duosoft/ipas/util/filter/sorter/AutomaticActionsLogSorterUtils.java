package bg.duosoft.ipas.util.filter.sorter;

import bg.duosoft.ipas.core.model.action.CAutomaticActionLogResult;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.AutomaticActionsLogFilter;

import java.time.Duration;
import java.util.Comparator;

public class AutomaticActionsLogSorterUtils {

    public static final String TIMER_NAME = "timerName";
    public static final String DATE_START = "dateStart";
    public static final String DATE_END = "dateEnd";
    public static final String DURATION = "duration";
    public static final String IS_ERROR = "is_error";

    public static Comparator<CAutomaticActionLogResult> getComparator(AutomaticActionsLogFilter filter) {
        Comparator<CAutomaticActionLogResult> comparator;
        switch (filter.getSortColumn()) {
            case TIMER_NAME:
                comparator = Comparator.comparing(CAutomaticActionLogResult::getTimerName);
                break;
            case DATE_START:
                comparator = Comparator.comparing(CAutomaticActionLogResult::getDateStart);
                break;
            case DATE_END:
                comparator = Comparator.comparing(CAutomaticActionLogResult::getDateEnd);
                break;
            case DURATION:
                comparator = Comparator.comparing(c -> Duration.between(c.getDateStart(), c.getDateEnd()));
                break;
            case IS_ERROR:
                comparator = Comparator.comparing(c -> c.isError());
                break;
            default:
                throw new RuntimeException("Unknown sorter column " + filter.getSortColumn());
        }
        if (Sortable.DESC_ORDER.equals(filter.getSortOrder())) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
