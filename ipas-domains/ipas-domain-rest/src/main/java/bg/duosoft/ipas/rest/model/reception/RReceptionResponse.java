package bg.duosoft.ipas.rest.model.reception;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.document.RDocumentSeqId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RReceptionResponse implements Serializable {
	private Integer docflowDocumentId;
	private RDocumentId docId;
	private RDocumentSeqId docSeqId;
	private RFileId fileId;
	private String externalSystemId;
	private List<RReceptionResponse> userdocReceptionResponses;
	public List<RReceptionResponse> getUserdocReceptionResponses() {
		if (userdocReceptionResponses == null) {
			userdocReceptionResponses = new ArrayList<>();
		}
		return userdocReceptionResponses;
	}
}

