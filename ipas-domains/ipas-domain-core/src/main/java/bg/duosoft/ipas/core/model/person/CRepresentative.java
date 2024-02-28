package bg.duosoft.ipas.core.model.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CRepresentative
        implements Serializable {
    private static final long serialVersionUID = -234605887436558667L;
    private String representativeType;
    private CPerson person;
    private Date attorneyPowerTerm;
    private Boolean reauthorizationRight;
    private Boolean priorReprsRevocation;
    private String authorizationCondition;

    public CRepresentative(String representativeType, CPerson person) {
        this.representativeType = representativeType;
        this.person = person;
    }
}


