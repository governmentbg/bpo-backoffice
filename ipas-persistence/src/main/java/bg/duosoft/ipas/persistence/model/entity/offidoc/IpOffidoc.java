package bg.duosoft.ipas.persistence.model.entity.offidoc;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfOffidocType;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_OFFIDOC", schema = "IPASPROD")
@Cacheable(value = false)
public class IpOffidoc implements Serializable {

    @EmbeddedId
    private IpOffidocPK pk;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Column(name = "ACTION_NBR")
    private Integer actionNbr;

    @ManyToOne
    @JoinColumn(name = "OFFIDOC_TYP", referencedColumnName = "OFFIDOC_TYP")
    private CfOffidocType offidocTyp;

    @Column(name = "PRINT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date printDate;

    @Column(name = "OFFIDOC_PROC_TYP")
    private String offidocProcTyp;

    @Column(name = "OFFIDOC_PROC_NBR")
    private Integer offidocProcNbr;

    @OneToOne
    @JoinColumn(name = "OFFIDOC_PROC_NBR", referencedColumnName = "PROC_NBR", insertable = false, updatable = false)
    @JoinColumn(name = "OFFIDOC_PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)
    private IpProcSimple ipProcSimple;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "OFFIDOC_ORI", referencedColumnName = "OFFIDOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "OFFIDOC_SER", referencedColumnName = "OFFIDOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "OFFIDOC_NBR", referencedColumnName = "OFFIDOC_NBR", insertable = false, updatable = false)
    })
    private IpOffidocPublishedDecision publishedDecision;

}
