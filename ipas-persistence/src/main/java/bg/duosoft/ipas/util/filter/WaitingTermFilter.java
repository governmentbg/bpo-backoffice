package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.WaitingTermSorterUtils;
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
public class WaitingTermFilter implements Sortable, Pageable {

    private String sortOrder = this.ASC_ORDER;
    private String sortColumn = WaitingTermSorterUtils.EXPIRATION_DATE;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private Integer responsibleUser;
    private String responsibleUserName;
    private String fileType;
    private String panelType;
    private String statusCode;

}
