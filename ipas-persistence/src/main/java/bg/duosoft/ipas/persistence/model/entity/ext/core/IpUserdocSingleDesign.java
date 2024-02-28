package bg.duosoft.ipas.persistence.model.entity.ext.core;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
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
@Table(name = "IP_USERDOC_SINGLE_DESIGN", schema = "EXT_CORE")
public class IpUserdocSingleDesign implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpUserdocSingleDesignsPK pk;

    @Column(name = "PRODUCT_TITLE")
    private String productTitle;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "APPL_TYP", referencedColumnName = "APPL_TYP"),
            @JoinColumn(name = "APPL_SUBTYP", referencedColumnName = "APPL_SUBTYP")
    })
    private CfApplicationSubtype cfApplicationSubtype;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocSingleDesignLocarnoClasses> locarnoClasses;

}
