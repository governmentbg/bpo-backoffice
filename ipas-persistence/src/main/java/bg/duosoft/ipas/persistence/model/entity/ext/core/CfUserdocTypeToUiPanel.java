package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "CF_USERDOC_TYPE_TO_UI_PANEL", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocTypeToUiPanel implements Serializable {

    @EmbeddedId
    private CfUserdocTypeToUiPanelPK pk;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "PANEL", referencedColumnName = "PANEL", insertable = false, updatable = false)
    private CfUserdocUiPanel userdocUiPanel;

}
