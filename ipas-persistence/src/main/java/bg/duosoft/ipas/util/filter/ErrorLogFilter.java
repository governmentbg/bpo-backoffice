package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.ErrorLogSorterUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLogFilter implements Sortable, Pageable {

    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = ErrorLogSorterUtils.DATE_CREATED;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private boolean resolved = false;
    private String priority;
    private String about;

}
