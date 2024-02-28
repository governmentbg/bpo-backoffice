package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "RECEPTION_TYPE_ADDITIONAL_USERDOC", schema = "EXT_RECEPTION")
public class ReceptionTypeAdditionalUserdoc {

    @EmbeddedId
    private ReceptionTypeAdditionalUserdocPK pk;

    @Column(name = "SEQ_NBR")
    private Integer seqNbr;

}
