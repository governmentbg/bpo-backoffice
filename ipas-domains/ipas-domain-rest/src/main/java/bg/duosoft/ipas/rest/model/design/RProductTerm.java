package bg.duosoft.ipas.rest.model.design;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.miscellaneous.CommonTerm;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RProductTerm implements CommonTerm, Serializable {
	private List<String> productClasses;
	private String termText;
	private String termStatus;
	 List<RProductTerm> matchedTerms;
	public List<String> getProductClasses() {
		if (productClasses == null) {
			productClasses = new ArrayList<>();
		}
		return productClasses;
	}
	public List<RProductTerm> getMatchedTerms() {
		if (matchedTerms == null) {
			matchedTerms = new ArrayList<>();
		}
		return matchedTerms;
	}
}

