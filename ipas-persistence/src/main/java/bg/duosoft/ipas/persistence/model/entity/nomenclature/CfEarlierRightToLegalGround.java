package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfEarlierRightToLegalGroundPK;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "CF_EARLIER_RIGHT_TO_LEGAL_GROUND", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfEarlierRightToLegalGround implements Serializable {

    @EmbeddedId
    private CfEarlierRightToLegalGroundPK pk;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "earlier_right_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CfEarlierRightTypes cfEarlierRightType;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "legal_ground_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CfLegalGroundTypes cfLegalGroundType;

}
