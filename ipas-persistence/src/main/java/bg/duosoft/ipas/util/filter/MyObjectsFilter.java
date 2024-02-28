package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.enums.FileType;
import bg.duosoft.ipas.util.filter.sorter.MyObjectsSorterUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyObjectsFilter implements Sortable,Pageable {
    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = MyObjectsSorterUtils.STATUS_DATE;
    private Integer responsibleUser;
    private String statusCode;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private Boolean newlyAllocated;
    private Boolean priorityRequest;
    private String responsibleUserName;
    private List<String> fileTypes;
    private String bordero;
    private String journalCode;
}
