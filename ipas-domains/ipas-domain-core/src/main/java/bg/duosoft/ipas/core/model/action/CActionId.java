package bg.duosoft.ipas.core.model.action;

import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.Data;

import java.io.Serializable;

@Data
public class CActionId implements Serializable {
    private static final long serialVersionUID = 3655898303089244995L;
    private Integer actionNbr;
    private CProcessId processId;

}
