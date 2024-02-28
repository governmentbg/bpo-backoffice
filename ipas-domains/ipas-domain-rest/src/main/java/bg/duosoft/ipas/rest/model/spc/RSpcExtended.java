package bg.duosoft.ipas.rest.model.spc;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RSpcExtended implements Serializable {
	private String bgPermitNumber;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date bgPermitDate;
	private String euPermitNumber;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date euPermitDate;
	private String productClaims;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date bgNotificationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date euNotificationDate;
}

