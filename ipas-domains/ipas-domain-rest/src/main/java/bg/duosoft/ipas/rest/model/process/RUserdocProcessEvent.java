package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.document.RDocumentSeqId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocProcessEvent implements Serializable {
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date filingDate;
	private String notes;
	private String userdocName;
	private String status;
	private RProcessId userdocProcessId;
	private RDocumentId documentId;
	private RDocumentSeqId documentSeqId;
	private RProcessId upperProcessId;
	private String applicantName;
	private String externalSystemId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date expirationDate;
	private RNextProcessAction nextProcessActionAfterExpirationDate;
}

