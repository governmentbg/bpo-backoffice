package bg.duosoft.ipas.rest.model.offidoc;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.process.RProcessParentData;
import bg.duosoft.ipas.rest.model.action.RActionId;
import bg.duosoft.ipas.rest.model.process.RProcessSimpleData;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ROffidoc implements Serializable {
	private ROffidocId offidocId;
	private RProcessId processId;
	private RProcessParentData offidocParentData;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date printDate;
	private String externalSystemId;
	private Integer abdocsDocumentId;
	private ROffidocType offidocType;
	private RActionId actionId;
	private RProcessSimpleData processSimpleData;
	private Boolean hasChildren;
}

