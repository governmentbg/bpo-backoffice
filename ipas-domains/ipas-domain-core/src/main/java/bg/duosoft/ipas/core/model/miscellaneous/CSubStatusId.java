package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.Data;

import java.io.Serializable;

@Data
public class CSubStatusId implements Serializable {

    private static final long serialVersionUID = 5033708711556093148L;
    private Integer substatusCode;
    private CStatusId statusId;
}

