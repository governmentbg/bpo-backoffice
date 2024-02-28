package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.enums.UserdocPersonRole;
import lombok.Data;

import java.io.Serializable;

@Data
public class CUserdocPersonRole implements Serializable {
    private UserdocPersonRole role;
    private Boolean indTakeFromOwner;
    private Boolean indTakeFromRepresentative;
    private Boolean indAdditionalOffidocCorrespondent;
    private String name;
}


