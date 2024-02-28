package bg.duosoft.ipas.rest.model.offidoc;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ROffidocType implements Serializable {
	private String offidocType;
	private String offidocName;
	private String defaultTemplate;
	private String direction;
	private Boolean hasPublication;
	private List<ROffidocTypeTemplate> templates;
	public List<ROffidocTypeTemplate> getTemplates() {
		if (templates == null) {
			templates = new ArrayList<>();
		}
		return templates;
	}
}

