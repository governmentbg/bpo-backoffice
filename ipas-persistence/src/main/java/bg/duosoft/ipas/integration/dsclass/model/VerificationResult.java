package bg.duosoft.ipas.integration.dsclass.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Taken from FO
 */
public enum VerificationResult {

	OK("OK"),

	HINT("HINT"),

	NOT_OK("NOT OK"),

	NOT_FOUND("NOT FOUND");

	private String value;

	VerificationResult(String value) {
		this.value = value;
	}

	@JsonCreator
	public static VerificationResult fromName(String name) {
		for (VerificationResult vr : VerificationResult.values()) {
			if (vr.value.equals(name)) {
				return vr;
			}
		}
		throw new IllegalArgumentException("VerificationResult not found: " + name);
	}
}
