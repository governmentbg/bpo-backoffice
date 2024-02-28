package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import bg.duosoft.ipas.persistence.model.entity.ext.core.CfEarlierRightToPanelPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocUiPanel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "CF_EARLIER_RIGHT_TO_PANEL", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfEarlierRightToPanel implements Serializable {

    @EmbeddedId
    private CfEarlierRightToPanelPK pk;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "earlier_right_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CfEarlierRightTypes cfEarlierRightType;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "PANEL", referencedColumnName = "PANEL", insertable = false, updatable = false)
    private CfUserdocUiPanel userdocUiPanel;
}
