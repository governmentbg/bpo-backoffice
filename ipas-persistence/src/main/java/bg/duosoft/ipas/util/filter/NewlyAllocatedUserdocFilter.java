package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.NewlyAllocatedUserdocSorterUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewlyAllocatedUserdocFilter implements Pageable, Sortable {
    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = NewlyAllocatedUserdocSorterUtils.USERDOC_DATE_CHANGED;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private String userdocType;
    private String userdocFilingNumber;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date userdocFilingDateFrom;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date userdocFilingDateTo;
    private String objectFileTyp;
    private String objectFileNbr;
    private Integer responsibleUser;
    private String responsibleUserName;
    private Boolean onlyActiveUsers = true;
}
