package bg.duosoft.ipas.core.model.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CReprsPowerOfAttorney {
    private Integer personNbr;
    private Integer addressNbr;
    private Integer personKind;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date attorneyPowerTerm;
    private Boolean reauthorizationRight;
    private Boolean priorReprsRevocation;
    private String authorizationCondition;
}
