package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_USERDOC_UI_PANEL", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfUserdocUiPanel implements Serializable {

    @Id
    @Column(name = "PANEL")
    private String panel;

    @Column(name = "IND_RECORDAL")
    private String indRecordal;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_EN")
    private String nameEn;

    @OneToMany
    @JoinColumn(name = "PANEL", referencedColumnName = "PANEL", insertable = false, updatable = false)
    private List<CfUserdocExtraDataToUiPanel> extraDataTypes;

}
