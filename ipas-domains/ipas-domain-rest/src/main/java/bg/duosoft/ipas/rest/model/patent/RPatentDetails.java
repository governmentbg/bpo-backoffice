package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPatentDetails implements Serializable {
	private Integer drawings;
	private Integer drawingPublications;
	private Integer descriptionPages;
	private Integer inventionsGroup;
	private Integer claims;
	private List<RPatentAttachment> patentAttachments;
	private Date notInForceDate;
	public List<RPatentAttachment> getPatentAttachments() {
		if (patentAttachments == null) {
			patentAttachments = new ArrayList<>();
		}
		return patentAttachments;
	}
}

