package bg.duosoft.ipas.rest.model.userdoc;

import bg.duosoft.ipas.rest.model.document.RDocumentSeqId;
import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocHierarchyNode {
	private RProcessId processId;
	private RDocumentId documentId;
	private RDocumentSeqId documentSeqId;
	private RProcessId upperProcessId;
	private String userdocType;
	private String userdocTypeName;
	private List<RUserdocHierarchyNode> children;
	private String externalSystemId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date filingDate;
	private RFileId fileId;
	private String efilingUser;
	public List<RUserdocHierarchyNode> getChildren() {
		if (children == null) {
			children = new ArrayList<>();
		}
		return children;
	}
	private String statusCode;
}

