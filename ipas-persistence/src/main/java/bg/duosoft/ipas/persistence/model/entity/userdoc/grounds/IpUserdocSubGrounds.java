package bg.duosoft.ipas.persistence.model.entity.userdoc.grounds;

import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocSubGroundsPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLegalGroundTypes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_USERDOC_SUB_GROUNDS", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpUserdocSubGrounds implements Serializable {
    @EmbeddedId
    private IpUserdocSubGroundsPK pk;

    @ManyToOne
    @JoinColumn(name = "LEGAL_GROUND_TYPE_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private CfLegalGroundTypes cfLegalGroundType;

}
