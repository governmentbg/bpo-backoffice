package bg.duosoft.ipas.core.model.reception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class CReceptionEuPatent implements Serializable {

    private String userdocType;//if null, no userdoc will be inserted at the end of reception!!!
    private Integer objectNumber;

}
