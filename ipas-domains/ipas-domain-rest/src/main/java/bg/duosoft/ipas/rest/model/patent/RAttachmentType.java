package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAttachmentType implements Serializable {
	private Integer id;
	private String name;
	private String attachmentNameSuffix;
	private Integer attachmentOrder;
	private String attachmentFileTypes;
	private String attachmentExtension;
	public RAttachmentType(Integer id) {
		this.id = id;
	}
}

