package bg.duosoft.ipas.persistence.model.entity.design;

import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentDrawings;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatentLocarnoClasses;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "IP_PATENT", schema = "IPASPROD")
@Cacheable(value = false)
public class SingleDesign implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpFilePK pk;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpPatentDrawings> singleDesignDrawings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private SingleDesignIpFile file;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "APPL_TYP", referencedColumnName = "APPL_TYP"),
            @JoinColumn(name = "APPL_SUBTYP", referencedColumnName = "APPL_SUBTYP"),
    })
    private CfApplicationSubtype cfApplicationSubtype;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "LAW_CODE")
    private Integer lawCode;

    @Column(name = "FILING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEPTION_DATE")
    private Date receptionDate;

    @Column(name = "ENGLISH_TITLE")
    private String englishTitle;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpPatentLocarnoClasses> ipPatentLocarnoClasses;

}

