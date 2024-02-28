package bg.duosoft.ipas.rest.model.mark;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.RApplicationSubType;
import bg.duosoft.ipas.rest.model.design.RLocarnoClasses;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocSingleDesign implements Serializable {
	private RDocumentId documentId;
	private RFileId fileId;
	private RApplicationSubType applicationSubType;
	private List<RLocarnoClasses> locarnoClasses;
	private String productTitle;
	public List<RLocarnoClasses> getLocarnoClasses() {
		if (locarnoClasses == null) {
			locarnoClasses = new ArrayList<>();
		}
		return locarnoClasses;
	}
}

