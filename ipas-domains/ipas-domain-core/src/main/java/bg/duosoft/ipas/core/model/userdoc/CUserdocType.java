package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.core.model.annotation.RestGenerationIgnore;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeConfig;
import bg.duosoft.ipas.enums.UserdocGroup;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class CUserdocType implements Serializable {
    private static final long serialVersionUID = -7529923033351823437L;
    private String userdocType;
    private String userdocName;
    private String generateProcTyp;
    private List<CUserdocPersonRole> roles;
    private List<CUserdocPanel> panels;
    private List<String> invalidatedUserdocTypes;
    private UserdocGroup userdocGroup;
    private Boolean indInactive;
    private Boolean indChangesOwner;
    private Boolean indChangesRepres;
    private Boolean indRenewal;
    @RestGenerationIgnore
    private CUserdocTypeConfig userdocTypeConfig;
}


