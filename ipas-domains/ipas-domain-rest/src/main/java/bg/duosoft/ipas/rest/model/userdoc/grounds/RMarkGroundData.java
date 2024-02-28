package bg.duosoft.ipas.rest.model.userdoc.grounds;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.rest.model.miscellaneous.RGeoCountry;
import bg.duosoft.ipas.rest.model.RApplicationSubType;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RMarkGroundData implements Serializable {
	private String registrationNbr;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date registrationDate;
	private Boolean niceClassesInd;
	private Boolean markImportedInd;
	private String nameText;
	private byte[] nameData;
	private String filingNumber;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date filingDate;
	private RMarkGroundType markGroundType;
	private RLegalGroundCategories legalGroundCategory;
	private RGeoCountry registrationCountry;
	private RApplicationSubType geographicalIndTyp;
	private MarkSignType markSignTyp;
	private List<RGroundNiceClasses> userdocGroundsNiceClasses;
	public List<RGroundNiceClasses> getUserdocGroundsNiceClasses() {
		if (userdocGroundsNiceClasses == null) {
			userdocGroundsNiceClasses = new ArrayList<>();
		}
		return userdocGroundsNiceClasses;
	}
}

