package bg.duosoft.ipas.util.filter;

import bg.duosoft.ipas.core.model.search.Sortable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersSyncFilter implements Sortable {
    private String sortOrder;
    private String sortColumn;
}
