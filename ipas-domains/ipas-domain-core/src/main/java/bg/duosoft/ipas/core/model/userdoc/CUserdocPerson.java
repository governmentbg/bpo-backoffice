package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.core.model.person.CPerson;
import bg.duosoft.ipas.enums.UserdocPersonRole;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CUserdocPerson implements Serializable {
    private UserdocPersonRole role;
    private CPerson person;
    private String notes;
    private Boolean indMain;
    private String representativeType;
    private Date attorneyPowerTerm;
    private Boolean reauthorizationRight;
    private Boolean priorReprsRevocation;
    private String authorizationCondition;
}
