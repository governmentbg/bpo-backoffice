package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.UserdocTypesSorterUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserdocTypesFilter implements Sortable, Pageable {
    private String sortOrder = this.ASC_ORDER;
    private String sortColumn = UserdocTypesSorterUtils.USERDOC_NAME;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private String userdocName;
    private Boolean indInactive;
}
