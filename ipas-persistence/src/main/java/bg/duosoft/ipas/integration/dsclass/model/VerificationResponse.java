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
public class VerificationResponse implements Serializable {

	@JsonProperty("VerificationResult")
	private VerificationResult verificationResult;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("MatchingResults")
	private List<MatchingResult> matchingResults;

    @Override
    public String toString() {
        return "VerificationResponse{" +
                "verificationResult=" + verificationResult +
                ", message='" + message + '\'' +
                ", matchingResults=" + matchingResults +
                '}';
    }
}
