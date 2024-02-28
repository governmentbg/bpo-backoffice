package bg.duosoft.ipas.persistence.model.entity.ext.acp;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "ACP_VIOLATION_PLACE", schema = "EXT_CORE")
@Cacheable(value = false)
public class AcpViolationPlace  implements Serializable {

    @EmbeddedId
    private AcpViolationPlacePK pk;

    @Column(name = "DESCRIPTION")
    private String description;
}
