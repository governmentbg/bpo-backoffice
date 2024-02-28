package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Pageable;
import bg.duosoft.ipas.core.model.search.Sortable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyUserdocsFilter implements Sortable,Pageable {
    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private String sortOrder = this.DESC_ORDER;
    private String sortColumn;
    private Integer responsibleUser;
    private String statusCode;
    private String userdocType;
    private String userdocGroupName;
    private String userdocFilingNumber;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date userdocFilingDateFrom;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date userdocFilingDateTo;
    private String objectFileTyp;
    private String objectFileNbr;
    private Boolean inProduction;
    private Boolean finished;
    private Boolean inTerm;
    private Boolean newlyAllocated;
    private String responsibleUserName;
}
