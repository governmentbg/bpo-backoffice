package bg.duosoft.ipas.core.model.reception;

import lombok.Data;

import java.io.Serializable;

@Data
public class CReceptionTypeAdditionalUserdoc implements Serializable {
    private Integer receptionType;
    private String userdocType;
    private String userdocTypeName;
    private Integer seqNbr;
}
