package bg.duosoft.ipas.rest.model.mark;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.enums.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RMarkAttachment implements Serializable {
	private Integer id;
	private String mimeType;
	private byte[] data;
	private String colourDescription;
	private String colourDescriptionInOtherLang;
	private String viewTitle;
	private Integer sequenceNumber;
	private List<RViennaClass> viennaClassList;
	private AttachmentType attachmentType;
	public List<RViennaClass> getViennaClassList() {
		if (viennaClassList == null) {
			viennaClassList = new ArrayList<>();
		}
		return viennaClassList;
	}
}

