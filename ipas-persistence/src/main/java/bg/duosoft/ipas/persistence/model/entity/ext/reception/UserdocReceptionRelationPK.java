package bg.duosoft.ipas.persistence.model.entity.ext.reception;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserdocReceptionRelationPK implements Serializable {

    @Column(name = "LINKED_USERDOC_TYPE")
    private String userdocType;

    @Column(name = "MAIN_TYPE")
    private String mainType;


}
