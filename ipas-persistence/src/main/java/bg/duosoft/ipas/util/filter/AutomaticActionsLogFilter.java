package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.AutomaticActionsLogSorterUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutomaticActionsLogFilter implements Sortable {
    private String sortOrder = this.ASC_ORDER;
    private String sortColumn = AutomaticActionsLogSorterUtils.DATE_START;

    private String timerName;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private LocalDate dateFrom;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private LocalDate dateTo;

    private Boolean error;
}
