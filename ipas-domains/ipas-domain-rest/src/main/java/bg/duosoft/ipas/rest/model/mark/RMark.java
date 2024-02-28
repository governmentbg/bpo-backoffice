package bg.duosoft.ipas.rest.model.mark;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RFile;
import bg.duosoft.ipas.rest.model.file.RRelationshipExtended;
import bg.duosoft.ipas.rest.model.efiling.REFilingData;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RMark implements Serializable {
	private Boolean indReadAttachments;
	private Integer rowVersion;
	private RMadridApplicationData madridApplicationData;
	private RProtectionData protectionData;
	private RFile file;
	private RSignData signData;
	private RLimitationData limitationData;
	private RRenewalData renewalData;
	private boolean reception;
	private RRelationshipExtended relationshipExtended;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date novelty1Date;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date novelty2Date;
	private REFilingData markEFilingData;
	private List<REnotifMark> enotifMarks;
	private String description;
	private RMarkInternationalReplacement markInternationalReplacement;
	private List<RInternationalNiceClass> internationalNiceClasses;
	private List<RMarkUsageRule> usageRules;

	public List<RMarkUsageRule> getUsageRules() {
		if (usageRules == null) {
			usageRules = new ArrayList<>();
		}
		return usageRules;
	}

	public List<REnotifMark> getEnotifMarks() {
		if (enotifMarks == null) {
			enotifMarks = new ArrayList<>();
		}
		return enotifMarks;
	}
	public List<RInternationalNiceClass> getInternationalNiceClasses() {
		if (internationalNiceClasses == null) {
			internationalNiceClasses = new ArrayList<>();
		}
		return internationalNiceClasses;
	}
}

