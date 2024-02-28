package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.action.RActionId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFileRecordal implements Serializable {
	private RFileId fileId;
	private RDocumentId documentId;
	private String externalSystemId;
	private RActionId actionId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date date;
	private String type;
	private RDocumentId invalidationDocumentId;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date invalidationDate;
	private String invalidationExternalSystemId;
	private RActionId invalidationActionId;
}

