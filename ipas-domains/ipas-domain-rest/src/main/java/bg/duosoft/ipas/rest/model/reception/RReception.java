package bg.duosoft.ipas.rest.model.reception;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RRepresentationData;
import bg.duosoft.ipas.rest.model.file.ROwnershipData;
import bg.duosoft.ipas.rest.model.document.RDocumentSeqId;
import bg.duosoft.ipas.rest.model.util.RAttachment;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RReception implements Serializable {
	private Integer docflowSystemId;
	private String docOri;
	private Integer submissionType;
	private Boolean originalExpected;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date entryDate;
	private RRepresentationData representationData;
	private ROwnershipData ownershipData;
	private RReceptionUserdoc userdoc;
	private RReceptionEuPatent euPatent;
	private RReceptionFile file;
	private String externalSystemId;
	private String notes;
	private Integer docflowEmailId;
	private boolean registerInDocflowSystem;
	private boolean registerReceptionRequest;
	private RDocumentSeqId documentSeqId;
	private List<RAttachment> attachments;
	private List<RReception> userdocReceptions;
	private boolean registerAsAdministrator;
	public List<RAttachment> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		return attachments;
	}
	public List<RReception> getUserdocReceptions() {
		if (userdocReceptions == null) {
			userdocReceptions = new ArrayList<>();
		}
		return userdocReceptions;
	}
}

