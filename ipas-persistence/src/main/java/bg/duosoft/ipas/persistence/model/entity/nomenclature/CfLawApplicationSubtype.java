package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CF_LAW_APPLICATION_SUBTYPE", schema = "IPASPROD")
@Getter
@Setter
@Cacheable(value = false)
public class CfLawApplicationSubtype implements Serializable {
    @EmbeddedId
    private CfLawApplicationSubtypePK pk;
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;
    @Column(name = "IND_FILE_NBR_SAMEAS_REG")
    private String indFileNbrSameasReg;
    @Column(name = "REGISTER_EXPIRATION_WCODE")
    private Long registerExpirationWcode;
    @Column(name = "REGISTRATION_YEAR")
    private Integer registrationYear;
    @Column(name = "RENEWAL_EXPIRATION_WCODE")
    private Long renewalExpirationWcode;
    @Column(name = "RENEWAL_YEARS")
    private Long renewalYears;
    @Column(name = "IND_ANNUITIES")
    private String indAnnuities;
    @Column(name = "IND_PUBLICATION")
    private String indPublication;
    @Column(name = "PUBLICATION_MONTHS")
    private Long publicationMonths;
    @Column(name = "PUBLICATION_PERIOD_TYP")
    private String publicationPeriodTyp;
    @Column(name = "IND_SPECIAL_PUBL_DATE")
    private String indSpecialPublDate;
    @Column(name = "IND_PUBL_IF_CLASS")
    private String indPublIfClass;
    @Column(name = "ANNUITY_EXPIRATION_WCODE")
    private Long annuityExpirationWcode;
    @Column(name = "DAYS_ANNUITY_EXPIRATION")
    private Integer daysAnnuityExpiration;
    @Column(name = "GRACE_MONTHS")
    private Long graceMonths;
    @Column(name = "ANNUITY_IGNORATION_WCODE")
    private Long annuityIgnorationWcode;
    @Column(name = "IGNORE_QTY_ANNUITIES")
    private Long ignoreQtyAnnuities;
    @Column(name = "MAX_RENEWALS")
    private Integer maxRenewals;
    @Column(name = "MONTHS_ANNUITY_EXPIRATION")
    private Long monthsAnnuityExpiration;
    @Column(name = "PUBLICATION_CODE")
    private String publicationCode;
    @Column(name = "XML_DESIGNER")
    private String xmlDesigner;
    @Column(name = "PUBLICATION_NBR_WCODE")
    private String publicationNbrWcode;
    @Column(name = "REGISTRATION_TYP")
    private String registrationTyp;
    @Column(name = "REGISTRATION_SERIES_WCODE")
    private Long registrationSeriesWcode;
    @Column(name = "FIXED_REGISTRATION_SERIES")
    private Integer fixedRegistrationSeries;
    @Column(name = "FIXED_PUBLICATION_SERIES")
    private String fixedPublicationSeries;
    @Column(name = "PUBLICATION_SERIES_WCODE")
    private Long publicationSeriesWcode;
    @Column(name = "ALW_REGIS_NBR_REGEN_FR_RENEW")
    private String alwRegisNbrRegenFrRenew;
    @Column(name = "PUBLICATION_TYP")
    private String publicationTyp;
}
