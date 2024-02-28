package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_LIST_OPTIONS", schema = "IPASPROD")
@IdClass(CfListOptionsPK.class)
@Cacheable(value = false)
public class CfListOptions implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "LIST_CODE")
    private String listCode;

    @Id
    @Column(name = "OPTION_NBR")
    private String optionNbr;

    @Column(name = "OPTION_NAME")
    private String optionName;

    @Column(name = "OPTION_LONG_NAME")
    private String optionLongName;

    @ManyToOne
    @JoinColumn(name = "RESTRICT_LAW_CODE", referencedColumnName = "LAW_CODE")
    private CfLaw restrictLawCode;

    @Column(name = "RESTRICT_PCT_WCODE")
    private String restrictPctWcode;

    @Column(name = "IND_REQUIRED")
    private String indRequired;

    @Column(name = "FILE_SEQ")
    private String fileSeq;

    @ManyToOne
    @JoinColumn(name = "RESTRICT_APPL_TYP", referencedColumnName = "APPL_TYP")
    private CfApplicationType restrictApplTyp;

    @Column(name = "IND_SPECIAL_OWNER_FORMAT")
    private String indSpecialOwnerFormat;

    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;

    @Column(name = "OPTION_MEDIUM_NAME")
    private String optionMediumName;

    @Column(name = "IND_INACTIVE")
    private String indInactive;

}
