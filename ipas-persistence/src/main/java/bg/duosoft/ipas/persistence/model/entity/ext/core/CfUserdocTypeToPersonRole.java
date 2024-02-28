package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "CF_USERDOC_TYPE_TO_PERSON_ROLE", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocTypeToPersonRole implements Serializable {

    @EmbeddedId
    private CfUserdocTypeToPersonRolePK pk;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "ROLE", referencedColumnName = "ROLE", insertable = false, updatable = false)
    private CfUserdocPersonRole userdocPersonRole;

}
