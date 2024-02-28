package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.person.RPerson;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocMainObjectData implements Serializable {
	private RFileId fileId;
	private List<RPerson> mainObjectOwners;
	public List<RPerson> getMainObjectOwners() {
		if (mainObjectOwners == null) {
			mainObjectOwners = new ArrayList<>();
		}
		return mainObjectOwners;
	}
}

