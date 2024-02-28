package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ReceptionUserdocCorrespondentPK implements Serializable {

    @Column(name = "RECEPTION_USERDOC_REQUEST_ID")
    private Integer receptionUserdocRequestId;

    @Column(name = "PERSON_NBR")
    private Integer personNbr;

    @Column(name = "ADDRESS_NBR")
    private Integer addressNbr;

}
