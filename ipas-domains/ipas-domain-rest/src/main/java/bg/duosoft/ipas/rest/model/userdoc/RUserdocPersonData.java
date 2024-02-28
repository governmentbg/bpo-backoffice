package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocPersonData implements Serializable {
	private List<RUserdocPerson> personList;
	public List<RUserdocPerson> getPersonList() {
		if (personList == null) {
			personList = new ArrayList<>();
		}
		return personList;
	}
}

