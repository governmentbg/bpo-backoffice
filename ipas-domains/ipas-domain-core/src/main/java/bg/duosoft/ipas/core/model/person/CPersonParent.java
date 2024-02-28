package bg.duosoft.ipas.core.model.person;

import lombok.Data;

import java.io.Serializable;

@Data
public class CPersonParent implements Serializable {
    private Integer personNbr;
    private Integer addressNbr;
    private Integer gralPersonIdNbr;
    private Integer gralPersonIdTyp;
    private String agentCode;
    private CPersonParent parentData;
}
