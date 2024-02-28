package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.enums.ActionTypeKind;
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
public class LastActionFilter implements Sortable,Pageable {

    private Integer page = this.DEFAULT_PAGE;
    private Integer pageSize = this.DEFAULT_PAGE_SIZE;
    private Integer actionTypeKind = ActionTypeKind.BOTH.code();
    private String fileType;
    private Integer responsibleUser;
    private String userdocType;
    private String userdocFilingNumber;
    private String objectFileNbr;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date actionDateFrom;
    @DateTimeFormat( pattern= "dd.MM.yyyy")
    private Date actionDateTo;
    private String responsibleUserName;
    private String sortOrder;
    private String sortColumn;
    private Integer captureUser;
    private String captureUserName;

}
