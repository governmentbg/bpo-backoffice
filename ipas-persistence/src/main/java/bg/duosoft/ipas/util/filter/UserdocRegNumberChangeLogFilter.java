package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.UserdocRegNumberChangeLogSorterUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserdocRegNumberChangeLogFilter implements Sortable, Pageable {

    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = UserdocRegNumberChangeLogSorterUtils.CHANGE_DATE;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private Integer responsibleUser;
}
