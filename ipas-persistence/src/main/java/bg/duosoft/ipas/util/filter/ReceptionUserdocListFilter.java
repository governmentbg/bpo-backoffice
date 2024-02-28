package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import bg.duosoft.ipas.util.filter.sorter.ReceptionUserdocSorterUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceptionUserdocListFilter implements Pageable, Sortable {
    private String sortOrder = this.DESC_ORDER;
    private String sortColumn = ReceptionUserdocSorterUtils.RECEPTION_USERDOC_CREATE_DATE;
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private String statusCode;
    private String userdocType;
    private String userdocFilingNumber;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date userdocFilingDateFrom;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date userdocFilingDateTo;
    private String objectFileTyp;
    private String objectFileNbr;
    private List<String> userdocTypeGroups;
}
