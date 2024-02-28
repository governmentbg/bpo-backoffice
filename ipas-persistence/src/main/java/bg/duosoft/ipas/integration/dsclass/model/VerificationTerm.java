package bg.duosoft.ipas.integration.dsclass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.Serializable;
import java.util.Optional;

/**
 * Taken from FO
 *
 */
@Getter
@Setter
public class VerificationTerm implements Serializable {

	@JsonProperty("LanguageCode")
	private String languageCode;

	@JsonProperty("TermText")
	private String termText;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + Optional.of(languageCode).map(String::hashCode).orElse(0);
		return prime * result + Optional.of(termText).map(String::hashCode).orElse(0);
	}

	@Override
	public boolean equals(Object obj) {
		final boolean isEquals;
		if (this == obj) {
			isEquals = true;
		} else if (obj == null) {
			isEquals = false;
		} else if (getClass() != obj.getClass()) {
			isEquals = false;
		} else {
			VerificationTerm other = (VerificationTerm) obj;
			isEquals = new EqualsBuilder().append(languageCode, other.languageCode).append(termText, other.termText).isEquals();
		}
		return isEquals;
	}
}
