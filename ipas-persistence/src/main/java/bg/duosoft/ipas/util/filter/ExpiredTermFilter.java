package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.ExpiredTermSortedUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpiredTermFilter implements Sortable,Pageable {
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private Integer responsibleUser;
    private String actionType;
    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = ExpiredTermSortedUtils.ACTION_DATE;
    private String responsibleUserName;
    private String fileType;
    private String panelType;
    private String statusCode;
}
