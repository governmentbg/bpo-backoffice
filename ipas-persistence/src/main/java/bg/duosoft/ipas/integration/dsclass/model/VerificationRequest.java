package bg.duosoft.ipas.integration.dsclass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Taken from FO
 *
 */
@Getter
@Setter
public class VerificationRequest implements Serializable {

	@JsonProperty("OfficeCode")
	private String officeCode;

	@JsonProperty("TermList")
	private List<VerificationTerm> termList;

	@JsonProperty("ClassificationList")
	private List<String> classificationList;
}
