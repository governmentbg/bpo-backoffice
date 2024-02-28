package bg.duosoft.ipas.rest.model.mark;

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
public class RNiceClass implements Serializable {
	private Integer niceClassEdition;
	private Integer niceClassNbr;
	private String niceClassGlobalStatus;
	private String niceClassDetailedStatus;
	private String niceClassDescription;
	private String niceClassDescriptionInOtherLang;
	private String niceClassVersion;
	private String niceEditionCalculated;
	private String niceVersionCalculated;
	private String niceClassInvalid;
	private List<RTerm> terms;
	private Boolean allTermsDeclaration;
	public List<RTerm> getTerms() {
		if (terms == null) {
			terms = new ArrayList<>();
		}
		return terms;
	}
}

