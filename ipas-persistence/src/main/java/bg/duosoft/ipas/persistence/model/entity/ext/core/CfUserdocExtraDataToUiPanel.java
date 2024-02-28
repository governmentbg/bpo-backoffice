package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "CF_USERDOC_EXTRA_DATA_TO_UI_PANEL", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocExtraDataToUiPanel implements Serializable {

    @EmbeddedId
    private CfUserdocExtraDataToUiPanelPK pk;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "CODE", referencedColumnName = "CODE", insertable = false, updatable = false)
    private CfUserdocExtraDataType extraDataType;
}
