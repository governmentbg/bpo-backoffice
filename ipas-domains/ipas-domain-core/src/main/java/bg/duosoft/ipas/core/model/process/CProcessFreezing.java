package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.Data;

import java.io.Serializable;

@Data
public class CProcessFreezing implements Serializable {
    private static final long serialVersionUID = -8092779897764863912L;
    private String freezingProcessDescription;
    private Boolean indContinueWhenEnd;
    private Boolean indNoOffidoc;
    private CProcessId freezingProcessId;

}