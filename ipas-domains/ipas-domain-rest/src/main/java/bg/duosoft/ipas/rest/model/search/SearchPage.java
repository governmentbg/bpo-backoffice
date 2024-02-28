package bg.duosoft.ipas.rest.model.search;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.search.Pageable;
import bg.duosoft.ipas.rest.model.search.Sortable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchPage implements Pageable, Sortable {
	private Integer page;
	private Integer pageSize;
	private String sortOrder;
	private String sortColumn;
}

