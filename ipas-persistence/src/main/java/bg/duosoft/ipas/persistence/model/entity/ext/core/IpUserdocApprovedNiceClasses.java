package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfClassNice;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Raya
 * 04.09.2020
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_USERDOC_APPROVED_NICE_CLASSES", schema = "EXT_CORE")
public class IpUserdocApprovedNiceClasses implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpUserdocApprovedNiceClassesPK pk;

    @Column(name = "NICE_CLASS_DESCRIPTION")
    private String niceClassDescription;

    @Column(name = "NICE_CLASS_EDITION")
    private Long niceClassEdition;

    @Column(name = "NICE_CLASS_DESCR_LANG2")
    private String niceClassDescrLang2;

    @Column(name = "NICE_CLASS_VERSION")
    private String niceClassVersion;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "NICE_CLASS_EDITION", referencedColumnName = "NICE_CLASS_EDITION", insertable = false, updatable = false),
        @JoinColumn(name = "NICE_CLASS_CODE", referencedColumnName = "NICE_CLASS_CODE", insertable = false, updatable = false),
        @JoinColumn(name = "NICE_CLASS_VERSION", referencedColumnName = "NICE_CLASS_VERSION", insertable = false, updatable = false)
    })
    private CfClassNice cfClassNice;
}
