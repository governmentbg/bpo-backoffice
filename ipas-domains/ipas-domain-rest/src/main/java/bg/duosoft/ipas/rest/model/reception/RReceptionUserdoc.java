package bg.duosoft.ipas.rest.model.reception;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocPerson;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RReceptionUserdoc implements Serializable {
	private RFileId fileId;
	private RDocumentId documentId;
	private String userdocType;
	private String externalRegistrationNumber;
	private List<RUserdocPerson> userdocPersons;
	private boolean withoutCorrespondents;
	public List<RUserdocPerson> getUserdocPersons() {
		if (userdocPersons == null) {
			userdocPersons = new ArrayList<>();
		}
		return userdocPersons;
	}
}

