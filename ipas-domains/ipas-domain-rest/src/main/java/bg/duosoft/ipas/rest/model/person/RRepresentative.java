package bg.duosoft.ipas.rest.model.person;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRepresentative implements Serializable {
	private String representativeType;
	private RPerson person;
	private Date attorneyPowerTerm;
	private Boolean reauthorizationRight;
	private Boolean priorReprsRevocation;
	private String authorizationCondition;
}

