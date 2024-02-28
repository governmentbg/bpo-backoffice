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
public class RSignData implements Serializable {
	private MarkSignType signType;
	private String markName;
	private String markTranslation;
	private String seriesDescription;
	private String markNameInOtherLang;
	private String markTransliteration;
	private String markTransliterationInOtherLang;
	private String markTranslationInOtherLang;
	private List<RMarkAttachment> attachments;
	public List<RMarkAttachment> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		return attachments;
	}
}

