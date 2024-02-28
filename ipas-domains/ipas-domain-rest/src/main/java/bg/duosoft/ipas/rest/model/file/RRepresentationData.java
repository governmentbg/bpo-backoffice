package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.person.RRepresentative;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRepresentationData implements Serializable {
	private List<RRepresentative> representativeList;
	private RDocumentId documentId_PowerOfAttorneyRegister;
	public List<RRepresentative> getRepresentativeList() {
		if (representativeList == null) {
			representativeList = new ArrayList<>();
		}
		return representativeList;
	}
}

