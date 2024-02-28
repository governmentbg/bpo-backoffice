package bg.duosoft.ipas.core.model.reception;

import lombok.Data;

import java.io.Serializable;

@Data
public class CProcessType implements Serializable {
    private String procTyp;
    private String procTypeName;
    private Integer relatedToWcode;
    private String primaryIniStatusCode;
    private String indInheritResponsible;
    private String secondaryIniStatusCode;
    private Long automaticProcessWcode;
    private String xmlDesigner;
    private String indUsed;
    private String functionName;
}
