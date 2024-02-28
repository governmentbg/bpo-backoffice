package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassLocarno;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_SINGLE_DESIGN_LOCARNO_CLASSES", schema = "EXT_CORE")
public class IpUserdocSingleDesignLocarnoClasses implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpUserdocSingleDesignLocarnoClassesPK pk;

    @Column(name = "LOCARNO_EDITION_CODE")
    private String locarnoEditionCode;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "LOCARNO_CLASS_CODE", referencedColumnName = "LOCARNO_CLASS_CODE", insertable = false, updatable = false),
            @JoinColumn(name = "LOCARNO_EDITION_CODE", referencedColumnName = "LOCARNO_EDITION_CODE", insertable = false, updatable = false)
    })
    private CfClassLocarno cfClassLocarno;
}
