package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.person.RAuthor;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAuthorshipData implements Serializable {
	private Boolean indOwnerSameAuthor;
	private List<RAuthor> authorList;
	public List<RAuthor> getAuthorList() {
		if (authorList == null) {
			authorList = new ArrayList<>();
		}
		return authorList;
	}
}

