package bg.duosoft.ipas.rest.model.search;

import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.miscellaneous.RStatus;
import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RFileId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RSearchResult implements Serializable {
	private RFileId pk;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date filingDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date registrationDate;
	private Integer registrationNbr;
	private String registrationDup;
	private String mainOwner;
	private String title;
	private Boolean hasImg;
	private RStatus status;
	private List<String> requestForValidationNbr;
	private RProcessId processId;
	private List<Long> niceClassCodes;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date expirationDate;
	private String representativeDetails;
	public List<String> getRequestForValidationNbr() {
		if (requestForValidationNbr == null) {
			requestForValidationNbr = new ArrayList<>();
		}
		return requestForValidationNbr;
	}
}

