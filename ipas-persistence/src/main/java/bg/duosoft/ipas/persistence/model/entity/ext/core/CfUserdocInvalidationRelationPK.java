package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CfUserdocInvalidationRelationPK implements Serializable {

    @Column(name = "USERDOC_TYPE")
    private String userdocType;

    @Column(name = "INVALIDATED_USERDOC_TYPE")
    private String invalidatedUserdocType;
}
