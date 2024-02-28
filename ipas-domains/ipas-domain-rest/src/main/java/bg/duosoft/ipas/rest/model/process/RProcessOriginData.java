package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.offidoc.ROffidocId;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RProcessOriginData implements Serializable {
	private Integer relatedToWorkCode;
	private RProcessId topProcessId;
	private RProcessId upperProcessId;
	private RFileId fileId;
	private String applicationType;
	private ROffidocId offidocId;
	private RDocumentId documentId;
	private RFileId userdocFileId;
	private String userdocType;
	private String manualProcDescription;
	private Integer manualProcRef;
}

