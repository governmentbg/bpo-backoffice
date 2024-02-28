package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.Data;

import java.io.Serializable;

@Data
public class CProcessFrozen implements Serializable {
    private static final long serialVersionUID = -6487020595041831350L;
    private String frozenProcessDescription;
    private Boolean indContinueWhenEnd;
    private Boolean indNoOffidoc;
    private CProcessId frozenProcessId;

}