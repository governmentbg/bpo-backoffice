package bg.duosoft.ipas.core.model.miscellaneous;

import bg.duosoft.ipas.enums.ProcessResultType;
import lombok.Data;

import java.io.Serializable;

@Data
public class CStatus implements Serializable {
    private static final long serialVersionUID = 8246205696720288497L;
    private String statusName;
    private Boolean indPending;
    private Boolean indResponsibleRequired;
    private CStatusId statusId;
    private Integer triggerActivityWcode;
    private String processResultType;
    public boolean isSecretPatentProcessResultType() {
        return processResultType != null && processResultType.equals(ProcessResultType.SECRET_PATENTS_UM.code());
    }

}
