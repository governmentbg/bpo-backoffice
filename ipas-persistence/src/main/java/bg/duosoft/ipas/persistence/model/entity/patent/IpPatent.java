package bg.duosoft.ipas.persistence.model.entity.patent;

import bg.duosoft.ipas.persistence.model.entity.IntellectualPropertyEntity;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpObjectEFilingData;
import bg.duosoft.ipas.persistence.model.entity.ext.plant.Plant;
import bg.duosoft.ipas.persistence.model.entity.ext.spc.SpcExtended;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.file.IpFileRelationshipExtended;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicantType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfApplicationSubtype;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfLaw;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.util.search.IpFileBridge;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "IP_PATENT", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpPatent implements Serializable, IntellectualPropertyEntity {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "file.pkSort", index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField(forField = "file.pkSort")
    @FieldBridge(impl = IpFileBridge.class)
    @IndexedEmbedded
    private IpFilePK pk;

    @Column(name = "RECEPTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receptionDate;

    @Column(name = "FILING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filingDate;

    @Column(name = "FIRST_PRIORITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstPriorityDate;

    @Column(name = "EXHIBITION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exhibitionDate;

    @Column(name = "SPECIAL_PUBL_APPL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date specialPublApplDate;

    @Column(name = "PUBLICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;

    @Column(name = "JOURNAL_CODE")
    private String journalCode;

    @Column(name = "ART2T_DOC_NBR")
    private Integer art2TDocNbr;

    @Column(name = "ART2T_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date art2TDate;

    @Column(name = "ART10T_DOC_NBR")
    private Integer art10TDocNbr;

    @Column(name = "ART10T_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date art10TDate;

    @Column(name = "REGISTRATION_NBR")
    private Integer registrationNbr;

    @Column(name = "REGISTRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(name = "IND_REGISTERED")
    private String indRegistered;

    @Column(name = "ENTITLEMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entitlementDate;

    @Column(name = "EXPIRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IND_OWNER_SAMEAS_INVENTOR")
    private String indOwnerSameasInventor;

    @Column(name = "POA_DOC_ORI")
    private String poaDocOri;

    @Column(name = "POA_LOG_TYP")
    private String poaLogTyp;

    @Column(name = "POA_DOC_SER")
    private Integer poaDocSer;

    @Column(name = "POA_DOC_NBR")
    private Integer poaDocNbr;

    @Column(name = "CAPTURE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date captureDate;

    @Column(name = "PCT_APPL_ID")
    private String pctApplId;

    @Column(name = "PCT_APPL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pctApplDate;

    @Column(name = "PCT_PUBL_COUNTRY")
    private String pctPublCountry;

    @Column(name = "PCT_PUBL_ID")
    private String pctPublId;

    @Column(name = "PCT_PUBL_TYP")
    private String pctPublTyp;

    @Column(name = "PCT_PUBL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pctPublDate;

    @Column(name = "PCT_PHASE")
    private Integer pctPhase;

    @Column(name = "ENGLISH_TITLE")
    private String englishTitle;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "PUBLICATION_NOTES")
    private String publicationNotes;

    @Column(name = "LAST_DESCRIPTION_PAGE_REF")
    private String lastDescriptionPageRef;

    @Column(name = "LAST_CLAIMS_PAGE_REF")
    private String lastClaimsPageRef;

    @Column(name = "SPECIAL_PUBL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date specialPublDate;

    @Column(name = "EXHIBITION_NOTES")
    private String exhibitionNotes;

    @Column(name = "REGISTRATION_TYP")
    private String registrationTyp;

    @Column(name = "REGISTRATION_SER")
    private Integer registrationSer;

    @Column(name = "REGISTRATION_DUP")
    private String registrationDup;

    @Column(name = "EXAM_IPC_USED")
    private String examIpcUsed;

    @Column(name = "EXAM_KEYWORDS_USED")
    private String examKeywordsUsed;

    @Column(name = "IND_EXAM_NOVELTY")
    private String indExamNovelty;

    @Column(name = "IND_EXAM_INVENTIVE")
    private String indExamInventive;

    @Column(name = "IND_EXAM_INDUSTRIAL")
    private String indExamIndustrial;

    @Column(name = "EXAM_RESULT")
    private String examResult;

    @Column(name = "IND_MANUAL_INTERPRETATION")
    private String indManualInterpretation;

    @Column(name = "REGIONAL_APPL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regionalApplDate;

    @Column(name = "REGIONAL_APPL_ID")
    private String regionalApplId;

    @Column(name = "REGIONAL_PUBL_COUNTRY")
    private String regionalPublCountry;

    @Column(name = "REGIONAL_PUBL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regionalPublDate;

    @Column(name = "REGIONAL_PUBL_ID")
    private String regionalPublId;

    @Column(name = "REGIONAL_PUBL_TYP")
    private String regionalPublTyp;

    @Column(name = "NOVELTY1_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date novelty1Date;

    @Column(name = "NOVELTY2_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date novelty2Date;

    @Column(name = "PUBLICATION_NBR")
    private Integer publicationNbr;

    @Column(name = "PUBLICATION_SER")
    private String publicationSer;

    @Column(name = "PUBLICATION_TYP")
    private String publicationTyp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded
    private IpFile file;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private IpObjectEFilingData patentEFilingData;

    @ManyToOne
    @JoinColumn(name = "LAW_CODE", referencedColumnName = "LAW_CODE")
    private CfLaw cfLaw;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "SERVICE_ADDR_NBR", referencedColumnName = "ADDR_NBR"),
            @JoinColumn(name = "SERVICE_PERSON_NBR", referencedColumnName = "PERSON_NBR")
    })
    @IndexedEmbedded(includePaths = {"ipPerson.personNameSort", "ipPerson.personNbr"})
    private IpPersonAddresses servicePerson; // todo mapping

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "MAIN_OWNER_ADDR_NBR", referencedColumnName = "ADDR_NBR"),
            @JoinColumn(name = "MAIN_OWNER_PERSON_NBR", referencedColumnName = "PERSON_NBR")
    })
    @IndexedEmbedded(includePaths = {"ipPerson.personNameSort"})
    private IpPersonAddresses mainOwner; // todo mapping

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "APPLICANT_TYP", referencedColumnName = "APPLICANT_TYP")
    })
    private CfApplicantType cfApplicantType;


    @ManyToOne
    @JoinColumn(name = "CAPTURE_USER_ID", referencedColumnName = "USER_ID")
    private IpUser captureUser;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "APPL_TYP", referencedColumnName = "APPL_TYP"),
            @JoinColumn(name = "APPL_SUBTYP", referencedColumnName = "APPL_SUBTYP"),
    })
    private CfApplicationSubtype cfApplicationSubtype;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @OrderBy("pk.claimNbr")
    private List<IpPatentClaims> ipPatentClaims;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @OrderBy("pk.refNumber")
    private List<IpPatentRefExam> ipPatentRefExams;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    private List<IpPatentDrawings> ipPatentDrawings;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded(includePaths = {"ipPersonAddresses.ipPerson.personNbr"} )
    private List<IpPatentInventors> ipPatentInventors;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @OrderBy("pk.ipcQualificationCode")
    private List<IpPatentIpcClasses> ipPatentIpcClasses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @OrderBy("pk.cpcQualificationCode")
    private List<IpPatentCpcClasses> ipPatentCpcClasses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded(includePaths = {"ipPersonAddresses.ipPerson.personNbr"} )
    private List<IpPatentOwners> owners;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private List<IpPatentPriorities> priorities;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    @IndexedEmbedded(includePaths = {"ipPersonAddresses.ipPerson.personNbr"} )
    private List<IpPatentReprs> representatives;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private List<IpPatentSummary> ipPatentSummaries;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    @IndexedEmbedded
    private Plant plantData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    @IndexedEmbedded
    private SpcExtended spcExtended;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private IpFileRelationshipExtended relationshipExtended;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
    })
    private IpPatentDetails patentDetails;

}
