package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.process.RProcessSimpleData;
import bg.duosoft.ipas.rest.model.person.RPerson;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFile implements Serializable {
	private String notes;
	private String title;
	private Integer rowVersion;
	private ROwnershipData ownershipData;
	private RPriorityData priorityData;
	private RFilingData filingData;
	private RRepresentationData representationData;
	private RRegistrationData registrationData;
	private List<RRelationship> relationshipList;
	private RFileId fileId;
	private RProcessId processId;
	private RProcessSimpleData processSimpleData;
	private RPublicationData publicationData;
	private RPerson servicePerson;
	private List<RFileRecordal> fileRecordals;
	public List<RRelationship> getRelationshipList() {
		if (relationshipList == null) {
			relationshipList = new ArrayList<>();
		}
		return relationshipList;
	}
	public List<RFileRecordal> getFileRecordals() {
		if (fileRecordals == null) {
			fileRecordals = new ArrayList<>();
		}
		return fileRecordals;
	}
}

