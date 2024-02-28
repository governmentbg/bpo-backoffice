package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.enums.*;
import bg.duosoft.ipas.rest.model.person.RPerson;
import java.io.Serializable;
import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocPerson implements Serializable {
	private UserdocPersonRole role;
	private RPerson person;
	private String notes;
	private Boolean indMain;
	private String representativeType;
	private Date attorneyPowerTerm;
	private Boolean reauthorizationRight;
	private Boolean priorReprsRevocation;
	private String authorizationCondition;
}

