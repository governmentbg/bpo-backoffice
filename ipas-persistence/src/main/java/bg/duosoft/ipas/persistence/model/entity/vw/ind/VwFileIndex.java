package bg.duosoft.ipas.persistence.model.entity.vw.ind;

import bg.duosoft.ipas.persistence.model.entity.process.IpProcPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 12.03.2021
 * Time: 16:55
 */
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class VwFileIndex {
    @Column(name = "APPL_SUBTYP")
    private String applSubTyp;

    @Column(name = "APPL_TYP")
    private String applTyp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENTITLEMENT_DATE")
    private Date entitlementDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    private Date filingDate;

    @Column(name = "DOC_FILES")
    private String docFiles;

    private IpProcPK procPk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;

    @Column(name = "REGISTRATION_DUP")
    private String registrationDup;

    @Column(name = "REGISTRATION_NBR")
    private Integer registrationNbr;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TITLE_LANG2")
    private String titleLang2;

    @Column(name = "MAIN_OWNER_PERSON_NAME")
    private String mainOwnerPersonName;

    @Column(name = "SERVICE_PERSON_NAME")
    private String servicePersonName;

    @Column(name = "SERVICE_PERSON_NBR")
    private Integer servicePersonNumber;
}
