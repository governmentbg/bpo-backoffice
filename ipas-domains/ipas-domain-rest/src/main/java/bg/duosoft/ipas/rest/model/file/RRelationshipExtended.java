package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRelationshipExtended implements Serializable {
	 String applicationType;
	 String filingNumber;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	 Date filingDate;
	 String registrationNumber;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	 Date registrationDate;
	 String registrationCountry;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	 Date cancellationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	 Date priorityDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	 Date serveMessageDate;
	 String relationshipType;
}

