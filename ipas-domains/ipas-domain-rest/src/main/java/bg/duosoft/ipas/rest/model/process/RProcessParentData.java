package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.offidoc.ROffidocId;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RProcessParentData implements Serializable {
	private RFileId fileId;
	private RDocumentId userdocId;
	private String userdocType;
	private ROffidocId offidocId;
	private String userdocRegistrationNumber;
	private String offidocRegistrationNumber;
	private RProcessId processId;
	private RProcessParentData parent;
	private RProcessId topProcessId;
	private RTopProcessFileData topProcessFileData;
	private Boolean isManualSubProcess;
}

