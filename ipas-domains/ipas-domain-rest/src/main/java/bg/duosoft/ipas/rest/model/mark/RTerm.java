package bg.duosoft.ipas.rest.model.mark;

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
public class RTerm implements CommonTerm, Serializable {
	private Integer classNumber;
	private String termText;
	private String termStatus;
	private String verificationResult;
	private List<RTerm> matchedTerms;
	public List<RTerm> getMatchedTerms() {
		if (matchedTerms == null) {
			matchedTerms = new ArrayList<>();
		}
		return matchedTerms;
	}
}

