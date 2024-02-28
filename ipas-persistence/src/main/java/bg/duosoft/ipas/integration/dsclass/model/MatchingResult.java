package bg.duosoft.ipas.integration.dsclass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Taken from FO
 *
 */
@Getter
@Setter
public class MatchingResult implements Serializable {

	@JsonProperty("TermId")
	private String termId;

	@JsonProperty(value = "TermClass")
	private String termClass;

	@JsonProperty(value="TermText", required = false)
	private String termText;

}
