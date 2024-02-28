package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.person.ROwner;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ROwnershipData implements Serializable {
	private List<ROwner> ownerList;
	public List<ROwner> getOwnerList() {
		if (ownerList == null) {
			ownerList = new ArrayList<>();
		}
		return ownerList;
	}
}

