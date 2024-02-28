package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocExtraData implements Serializable {
	private RDocumentId documentId;
	private RUserdocExtraDataType type;
	private String textValue;
	private Integer numberValue;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dateValue;
	private Boolean booleanValue;
}

