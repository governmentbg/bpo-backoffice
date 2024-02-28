package bg.duosoft.ipas.persistence.model.entity.mark;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyEntity;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpObjectEFilingData;
import bg.duosoft.ipas.persistence.model.entity.ext.acp.*;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpInternationalNiceClasses;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipExtended;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLaw;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.util.search.IntregnBridge;
import bg.duosoft.ipas.util.search.IpFileBridge;
import bg.duosoft.ipas.util.search.ObjectToFlagBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_MARK", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpMark implements Serializable, IntellectualPropertyEntity {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "file.pkSort", index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField(forField = "file.pkSort")
    @FieldBridge(impl = IpFileBridge.class)
    @IndexedEmbedded
    private IpFilePK pk;

    @Column(name = "FILING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filingDate;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "SIGN_WCODE")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String signWcode;

    @Column(name = "DISCLAIMER")
    private String disclaimer;

    @Column(name = "TRANSLATION")
    private String translation;

    @Column(name = "NICE_CLASS_TXT")
    private String niceClassTxt;

    @Column(name = "FIRST_PRIORITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstPriorityDate;

    @Column(name = "IND_REGISTERED")
    private String indRegistered;

    @Column(name = "REGISTRATION_NBR")
    private Integer registrationNbr;

    @Column(name = "REGISTRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(name = "ENTITLEMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entitlementDate;

    @Column(name = "EXPIRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Column(name = "CAPTURE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureDate;

    @Column(name = "PUBLICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;

    @Column(name = "LAST_RENEWAL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRenewalDate;

    @Column(name = "REGISTRATION_DUP")
    private String registrationDup;

    @Column(name = "REGISTRATION_TYP")
    private String registrationTyp;

    @Column(name = "REGISTRATION_SER")
    private Integer registrationSer;

    @Column(name = "JOURNAL_CODE")
    private String journalCode;

    @Column(name = "RNEW1_FILE_SEQ")
    private String rnew1FileSeq;

    @Column(name = "RNEW1_FILE_TYP")
    private String rnew1FileTyp;

    @Column(name = "RNEW1_FILE_SER")
    private Integer rnew1FileSer;

    @Column(name = "RNEW1_FILE_NBR")
    private Integer rnew1FileNbr;

    @Column(name = "RNEW2_FILE_SEQ")
    private String rnew2FileSeq;

    @Column(name = "RNEW2_FILE_TYP")
    private String rnew2FileTyp;

    @Column(name = "RNEW2_FILE_SER")
    private Integer rnew2FileSer;

    @Column(name = "RNEW2_FILE_NBR")
    private Integer rnew2FileNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INT_FILING_DATE")
    private Date intFilingDate;

    @Column(name = "ASSOCIATION_NBR")
    private Integer associationNbr;

    @Column(name = "MARK_SERIES_DESCRIPTION")
    private String markSeriesDescription;

    @Column(name = "BY_CONSENT_DESCRIPTION")
    private String byConsentDescription;

    @Column(name = "IND_MANUAL_INTERPRETATION")
    private String indManualInterpretation;

    @Column(name = "PUBLICATION_NOTES")
    private String publicationNotes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXHIBITION_DATE")
    private Date exhibitionDate;

    @Column(name = "EXHIBITION_NOTES")
    private String exhibitionNotes;

    @Column(name = "BASIC_FILE_REF")
    private String basicFileRef;

    @Column(name = "INTREGN")
    @Field(name = "intregnNumber", index = Index.YES, analyze = Analyze.NO, store = Store.NO, bridge = @FieldBridge(impl = IntregnBridge.class))
//    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String intregn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NOVELTY1_DATE")
    private Date novelty1Date;

    @Column(name = "NOVELTY2_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date novelty2Date;

    @Column(name = "DISCLAIMER_LANG2")
    private String disclaimerLang2;

    @Column(name = "TRANSLATION_LANG2")
    private String translationLang2;

    @Column(name = "MARK_TRANSLITERATION")
    private String markTransliteration;

    @Column(name = "MARK_TRANSLITERATION_LANG2")
    private String markTransliterationLang2;

    @Column(name = "PUBLICATION_NBR")
    private Integer publicationNbr;

    @Column(name = "PUBLICATION_SER")
    private String publicationSer;

    @Column(name = "PUBLICATION_TYP")
    private String publicationTyp;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "APPL_TYP", referencedColumnName = "APPL_TYP"),
            @JoinColumn(name = "APPL_SUBTYP", referencedColumnName = "APPL_SUBTYP"),
    })
    private CfApplicationSubtype cfApplicationSubtype;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "MARK_CODE", referencedColumnName = "MARK_CODE")
    private IpName name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    @IndexedEmbedded
    private IpFile file;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    @Field(name = "img", index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    @FieldBridge(impl = ObjectToFlagBridge.class)
    private IpLogo logo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private IpMarkRegulation ipMarkRegulation;

    @OrderBy(value = "orderNbr ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false)
    })
    @IndexedEmbedded(includePaths = {"ipPersonAddresses.ipPerson.personNbr"})
    private List<IpMarkOwners> owners;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private List<IpMarkPriorities> priorities;

    @OrderBy(value = "pk.niceClassCode ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded(includePaths = "pk")
    private List<IpMarkNiceClasses> ipMarkNiceClasses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpInternationalNiceClasses> ipInternationalNiceClasses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<EnotifMark> enotifMarks;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded(includePaths = {"ipPersonAddresses.ipPerson.personNbr"})
    private List<IpMarkReprs> representatives;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "SERVICE_ADDR_NBR", referencedColumnName = "ADDR_NBR"),
            @JoinColumn(name = "SERVICE_PERSON_NBR", referencedColumnName = "PERSON_NBR")
    })
    @IndexedEmbedded(includePaths = {"ipPerson.personNameSort", "ipPerson.personNbr"})
    private IpPersonAddresses servicePerson;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "MAIN_OWNER_ADDR_NBR", referencedColumnName = "ADDR_NBR"),
            @JoinColumn(name = "MAIN_OWNER_PERSON_NBR", referencedColumnName = "PERSON_NBR")
    })
    @IndexedEmbedded(includePaths = {"ipPerson.personNameSort"})
    private IpPersonAddresses mainOwner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private IpObjectEFilingData markEFilingData;

    @ManyToOne
    @JoinColumn(name = "LAW_CODE", referencedColumnName = "LAW_CODE")
    private CfLaw cfLaw;

    @ManyToOne
    @JoinColumn(name = "CAPTURE_USER_ID", referencedColumnName = "USER_ID")
    private IpUser captureUser;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private IpFileRelationshipExtended relationshipExtended;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpMarkAttachment> attachments;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpMarkUsageRule> usageRules;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<AcpAffectedObject> acpAffectedObjects;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<AcpViolationPlace> acpViolationPlaces;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private AcpDetails acpDetails;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<AcpTakenItem> acpTakenItems;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<AcpCheckReason> acpCheckReasons;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private AcpCheckData acpCheckData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private AcpAdministrativePenalty acpAdministrativePenalty;



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<AcpReprs> acpRepresentatives;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private AcpServicePerson acpServicePerson;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private AcpInfringerPerson acpInfringerPerson;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private IpMarkInternationalReplacement markInternationalReplacement;

}
