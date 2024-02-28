package bg.duosoft.ipas.persistence.model.entity.userdoc;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocPK;
import bg.duosoft.ipas.persistence.model.entity.ext.core.*;
import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.model.entity.userdoc.court_appeals.IpUserdocCourtAppeal;
import bg.duosoft.ipas.persistence.model.entity.userdoc.grounds.IpUserdocRootGrounds;
import bg.duosoft.ipas.persistence.model.entity.userdoc.reviewers.IpUserdocReviewer;
import bg.duosoft.ipas.util.search.IpDocFileFieldBridge;
import bg.duosoft.ipas.util.search.IpDocPKBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Indexed
@Table(name = "IP_USERDOC", schema = "IPASPROD")
@Cacheable(value = false)
public class IpUserdoc implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @FieldBridge(impl = IpDocPKBridge.class)
    private IpDocPK pk;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "CAPTURE_USER_ID", referencedColumnName = "USER_ID")
    private IpUser captureUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CAPTURE_DATE")
    private Date captureDate;

    @Column(name = "APPLICANT_NOTES")
    private String applicantNotes;

    @Column(name = "COURT_DOC_NBR")
    private Integer courtDocNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COURT_DOC_DATE")
    private Date courtDocDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DECREE_DATE")
    private Date decreeDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CANCELLATION_DATE")
    private Date cancellationDate;

    @Column(name = "AUXI_REG_REGIS_DOC_ORI")
    private String auxiRegRegisDocOri;

    @Column(name = "AUXI_REG_REGIS_LOG_TYP")
    private String auxiRegRegisLogTyp;

    @Column(name = "AUXI_REG_REGIS_DOC_SER")
    private Integer auxiRegRegisDocSer;

    @Column(name = "AUXI_REG_REGIS_DOC_NBR")
    private Integer auxiRegRegisDocNbr;

    @Column(name = "PAYEE_PERSON_NBR")
    private Integer payeePersonNbr;

    @Column(name = "PAYEE_ADDR_NBR")
    private Integer payeeAddrNbr;

    @Column(name = "PAYER_PERSON_NBR")
    private Integer payerPersonNbr;

    @Column(name = "PAYER_ADDR_NBR")
    private Integer payerAddrNbr;

    @Column(name = "GRANTOR_PERSON_NBR")
    private Integer grantorPersonNbr;

    @Column(name = "GRANTOR_ADDR_NBR")
    private Integer grantorAddrNbr;

    @Column(name = "GRANTEE_PERSON_NBR")
    private Integer granteePersonNbr;

    @Column(name = "GRANTEE_ADDR_NBR")
    private Integer granteeAddrNbr;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "APPLICANT_ADDR_NBR", referencedColumnName = "ADDR_NBR"),
            @JoinColumn(name = "APPLICANT_PERSON_NBR", referencedColumnName = "PERSON_NBR")
    })
    private IpPersonAddresses servicePerson;

    @Column(name = "IND_EXCLUSIVE_LICENSE")
    private String indExclusiveLicense;

    @Column(name = "IND_COMPULSORY_LICENSE")
    private String indCompulsoryLicense;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "QTY_PAGES")
    private Integer qtyPages;

    @Column(name = "POA_DOC_ORI")
    private String poaDocOri;

    @Column(name = "POA_LOG_TYP")
    private String poaLogTyp;

    @Column(name = "POA_DOC_SER")
    private Integer poaDocSer;

    @Column(name = "POA_DOC_NBR")
    private Integer poaDocNbr;

    @Column(name = "RECEIPT_NBR")
    private Integer receiptNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEIPT_DATE")
    private Date receiptDate;

    @Column(name = "RECEIPT_AMOUNT")
    private BigDecimal receiptAmount;

    @Column(name = "COURT_DESCRIPTION")
    private String courtDescription;

    @Column(name = "COURT_ADDRESS")
    private String courtAddress;

    @Column(name = "COURT_FILE_LETTER")
    private String courtFileLetter;

    @Column(name = "COURT_FILE_YEAR")
    private Integer courtFileYear;

    @Column(name = "COURT_FILE_NBR")
    private Integer courtFileNbr;

    @Column(name = "COURT_FILE_DESCRIPTION")
    private String courtFileDescription;

    @Column(name = "COURT_DOC_LETTER")
    private String courtDocLetter;

    @Column(name = "COURT_DOC_YEAR")
    private Integer courtDocYear;

    @Column(name = "DECREE_YEAR")
    private Integer decreeYear;

    @Column(name = "DECREE_NBR")
    private Integer decreeNbr;

    @Column(name = "CONTRACT_SUMMARY")
    private String contractSummary;

    @Column(name = "AFFECTED_DOC_ORI")
    private String affectedDocOri;

    @Column(name = "AFFECTED_DOC_LOG")
    private String affectedDocLog;

    @Column(name = "AFFECTED_DOC_SER")
    private Integer affectedDocSer;

    @Column(name = "AFFECTED_DOC_NBR")
    private Integer affectedDocNbr;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocPerson> persons;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI"),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG"),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER"),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR")
    })
    @MapsId
    @IndexedEmbedded(includePaths = {
            "proc.pk",
            "proc.subProc.fileSeq",
            "proc.subProc.fileTyp",
            "proc.subProc.fileSer",
            "proc.subProc.fileNbr",
            "proc.userdocTyp",
            "proc.statusCode",
            "filingDate",
            "filingDateSort",
            "externalSystemId"})
    @Field(name = "file.pkSort", analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpDocFileFieldBridge.class), indexNullAs = "")
    @SortableField(forField = "file.pkSort")
    private IpDoc ipDoc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocRootGrounds> userdocRootGrounds;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocCourtAppeal> userdocCourtAppeals;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocReviewer> reviewers;

    @OneToMany(cascade ={CascadeType.PERSIST, CascadeType.REFRESH,CascadeType.DETACH})//no  merge, because on userdoc update we are not filling all the IpUserdocTypes details. They are filled only on insertion!
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocTypes> ipUserdocTypes;

    @OrderBy("pk.niceClassCode ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocNiceClasses> ipUserdocNiceClasses;

    @OrderBy("niceClassCode ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpInternationalNiceClasses> ipInternationalNiceClasses;

    @OrderBy("pk.niceClassCode ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
        @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
        @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
        @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
        @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocApprovedNiceClasses> ipUserdocApprovedNiceClasses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocSingleDesign> ipUserdocSingleDesigns;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private List<IpUserdocExtraData> userdocExtraData;
}
