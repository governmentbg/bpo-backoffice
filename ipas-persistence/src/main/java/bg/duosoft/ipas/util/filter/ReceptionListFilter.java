package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Sortable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceptionListFilter implements Sortable {
    private String sortOrder;
    private String sortColumn;
}
