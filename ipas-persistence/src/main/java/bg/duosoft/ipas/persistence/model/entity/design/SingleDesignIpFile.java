package bg.duosoft.ipas.persistence.model.entity.design;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "IP_FILE", schema = "IPASPROD")
@Cacheable(value = false)
public class SingleDesignIpFile implements Serializable {

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "PROC_NBR")
    private Integer procNbr;

    @Column(name = "APPL_SUBTYP")
    private String applSubtyp;

    @Column(name = "APPL_TYP")
    private String applTyp;

    @Column(name = "LAW_CODE")
    private Integer lawCode;

    @Column(name = "FILING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filingDate;

    @OneToOne
    @JoinColumn(name = "PROC_NBR", referencedColumnName = "PROC_NBR", insertable = false, updatable = false)
    @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)
    private IpProcSimple ipProcSimple;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", updatable = false),
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", updatable = false)}
    )
    private SingleDesignIpDoc ipDoc;

}
