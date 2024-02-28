package bg.duosoft.ipas.persistence.model.entity.userdoc.grounds;


import bg.duosoft.ipas.persistence.model.entity.ext.ExtCfSignType;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocRootGroundsPK;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfGeoCountry;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLegalGroundCategories;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfMarkGroundType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_MARK_GROUND_DATA", schema = "EXT_CORE")
@Cacheable(value = false)
public class IpMarkGroundData implements Serializable {

    @EmbeddedId
    private IpUserdocRootGroundsPK pk;

    @Column(name = "REGISTRATION_NBR")
    private String registrationNbr;

    @Column(name = "REGISTRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(name = "NICE_CLASSES_IND")
    private String niceClassesInd;

    @Column(name = "MARK_IMPORTED_IND")
    private String markImportedInd;

    @Column(name = "NAME_TEXT")
    private String nameText;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "NAME_DATA")
    private byte[] nameData;

    @Column(name = "FILING_NUMBER")
    private String filingNumber;

    @Column(name = "FILING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filingDate;

    @ManyToOne
    @JoinColumn(name = "GROUND_MARK_TYPE_ID", referencedColumnName = "id")
    private CfMarkGroundType markGroundType;

    @ManyToOne
    @JoinColumn(name = "REGISTRATION_COUNTRY", referencedColumnName = "COUNTRY_CODE")
    @IndexedEmbedded
    private CfGeoCountry registrationCountry;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_CODE", referencedColumnName = "code")
    private CfLegalGroundCategories legalGroundCategory;

    @ManyToOne
    @JoinColumn(name = "MARK_SIGN_TYPE", referencedColumnName = "SIGN_TYPE")
    private ExtCfSignType markSignTyp;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "GI_APPL_TYP", referencedColumnName = "APPL_TYP"),
            @JoinColumn(name = "GI_APPL_SUBTYP", referencedColumnName = "APPL_SUBTYP")
    })
    private CfApplicationSubtype geographicalIndTyp;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ROOT_GROUND_ID", referencedColumnName = "ROOT_GROUND_ID", insertable = false, updatable = false)
    })
    private List<IpUserdocGroundsNiceClasses> userdocGroundsNiceClasses;


    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "ROOT_GROUND_ID", referencedColumnName = "ROOT_GROUND_ID", insertable = false, updatable = false)
    })
    private IpUserdocRootGrounds rootGround;

}
