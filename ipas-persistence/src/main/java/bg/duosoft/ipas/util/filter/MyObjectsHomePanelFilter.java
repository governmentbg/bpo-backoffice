package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Sortable;
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
public class MyObjectsHomePanelFilter implements Sortable {
    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = MyObjectsSorterUtils.STATUS_DATE;
    private Integer responsibleUser;
    private Integer pageSize;
    private List<String> fileTypes;
}
