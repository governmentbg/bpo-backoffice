package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ReceptionTypeAdditionalUserdocPK implements Serializable {

    @Column(name = "RECEPTION_TYPE")
    private Integer receptionType;

    @Column(name = "USERDOC_TYPE")
    private String userdocType;

}
