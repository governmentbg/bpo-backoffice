package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.Data;

import java.io.Serializable;

@Data
public class CSubstatus implements Serializable {
    private static final long serialVersionUID = -4383562769875993474L;
    private String substatusName;
    private CSubStatusId subStatus;
}
