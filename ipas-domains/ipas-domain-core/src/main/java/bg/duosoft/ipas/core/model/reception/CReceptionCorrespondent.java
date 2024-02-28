package bg.duosoft.ipas.core.model.reception;

import lombok.Data;

import java.io.Serializable;

@Data
public class CReceptionCorrespondent implements Serializable {
    private Integer receptionRequestId;
    private Integer receptionUserdocRequestId;
    private Integer personNbr;
    private Integer addressNbr;
    private CCorrespondentType correspondentType;
    private String representativeType;
}
