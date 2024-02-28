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
public class RRegistrationData implements Serializable {
	private Boolean indRegistered;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date registrationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date entitlementDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date expirationDate;
	private RRegistrationId registrationId;
}

