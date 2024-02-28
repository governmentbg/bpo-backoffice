package bg.duosoft.ipas.persistence.model.entity.nomenclature;


import bg.duosoft.ipas.persistence.model.entity.ext.core.CfLegalGroundTypeToUiPanelPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.CfUserdocUiPanel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "CF_LEGAL_GROUND_TYPE_TO_UI_PANEL", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfLegalGroundTypeToUiPanel implements Serializable {
    @EmbeddedId
    private CfLegalGroundTypeToUiPanelPK pk;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "legal_ground_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CfLegalGroundTypes cfLegalGroundType;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "PANEL", referencedColumnName = "PANEL", insertable = false, updatable = false)
    private CfUserdocUiPanel userdocUiPanel;

}
