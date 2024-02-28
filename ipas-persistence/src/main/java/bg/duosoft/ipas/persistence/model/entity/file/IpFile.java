package bg.duosoft.ipas.persistence.model.entity.file;

import bg.duosoft.ipas.persistence.model.entity.doc.IpDoc;
import bg.duosoft.ipas.persistence.model.entity.doc.IpDocFiles;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordal;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfFileType;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "IP_FILE", schema = "IPASPROD")
@NormalizerDef(
        name = "sortNormalizer",
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
        }
)
@Cacheable(value = false)
public class IpFile implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    private IpFilePK pk;

    @Column(name = "FILE_SOURCE_WCODE")
    private Long fileSourceWcode;

    @Column(name = "PROC_TYP")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @FieldBridge(impl = IntegerBridge.class)
    private String procTyp;

    @Column(name = "PROC_NBR")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @FieldBridge(impl = IntegerBridge.class)
    private Integer procNbr;

    @Column(name = "APPL_TYP")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String applTyp;

    @Column(name = "IND_FAX_ONLY")
    private String indFaxOnly;

    @Column(name = "LAW_CODE")
    private Integer lawCode;

    @Column(name = "REGISTRATION_NBR")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @NumericField(forField = "registrationNbr")
    @Field(name = "registrationNbrSort",
            index= Index.YES,
            analyze = Analyze.NO,
            store = Store.YES,
            bridge = @FieldBridge(impl = IntegerBridge.class),
            normalizer = @Normalizer(definition = "sortNormalizer"))
    @SortableField(forField = "registrationNbrSort")
    private Integer registrationNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REGISTRATION_DATE")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    private Date registrationDate;

    @Column(name = "TITLE")
    @Field(analyze = Analyze.YES, index = Index.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(definition = "WordAnalyzer"))
    @Field(name = "titleExact", analyze = Analyze.NO, index = Index.YES, store = Store.NO, indexNullAs = "", norms = Norms.NO)
    @Field(name = "titleCustom", analyze = Analyze.YES, index = Index.YES, store = Store.NO, indexNullAs = "", analyzer = @Analyzer(definition = "FullTextAnalyzer"))
    @Field(name = "titleSort", index= Index.NO, analyze = Analyze.NO, store = Store.YES, indexNullAs = "", normalizer = @Normalizer(definition = "sortNormalizer"))
    @SortableField(forField = "titleSort")
    private String title;

    @Column(name = "APPL_SUBTYP")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String applSubtyp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    @Field(name = "filingDateSort", index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    @SortableField(forField = "filingDateSort")
    private Date filingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FIRST_PRIORITY_DATE")
    private Date firstPriorityDate;

    @Column(name = "IND_REGISTERED")
    private String indRegistered;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENTITLEMENT_DATE")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    private Date entitlementDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRATION_DATE")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    private Date expirationDate;

    @Column(name = "CAPTURE_USER_ID")
    private Integer captureUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CAPTURE_DATE")
    private Date captureDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PUBLICATION_DATE")
    private Date publicationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SPECIAL_PUBL_DATE")
    private Date specialPublDate;

    @Column(name = "JOURNAL_CODE")
    private String journalCode;

    @Column(name = "REGISTRATION_DUP")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES, indexNullAs = "")
    private String registrationDup;

    @Column(name = "REGISTRATION_TYP")
    private String registrationTyp;

    @Column(name = "REGISTRATION_SER")
    private Integer registrationSer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VALIDATION_DATE")
    private Date validationDate;

    @Column(name = "VALIDATION_USER_ID")
    private Integer validationUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INT_FILING_DATE")
    private Date intFilingDate;

//    @Column(name = "RNEW_FILE_SEQ")
//    private String rnewFileSeq;

//    @Column(name = "RNEW_FILE_TYP")
//    private String rnewFileTyp;

//    @Column(name = "RNEW_FILE_SER")
//    private Integer rnewFileSer;

//    @Column(name = "RNEW_FILE_NBR")
//    private Integer rnewFileNbr;

    @Column(name = "IND_FOREIGN")
    private String indForeign;

    @Field(name = "englishTitle", analyze = Analyze.YES, index = Index.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(definition = "WordAnalyzer"))
    @Field(name = "englishTitleExact", analyze = Analyze.NO, index = Index.YES, store = Store.NO, indexNullAs = "", norms = Norms.NO)
    @Field(name = "englishTitleCustom", analyze = Analyze.YES, index = Index.YES, store = Store.NO, indexNullAs = "", analyzer = @Analyzer(definition = "FullTextAnalyzer"))
    @Field(name = "englishTitleSort", index= Index.NO, analyze = Analyze.NO, store = Store.YES, indexNullAs = "", normalizer = @Normalizer(definition = "sortNormalizer"))
    @SortableField(forField = "englishTitleSort")
    @Column(name = "TITLE_LANG2")
    private String titleLang2;

    @Column(name = "PUBLICATION_NBR")
    private Integer publicationNbr;

    @Column(name = "PUBLICATION_SER")
    private String publicationSer;

    @Column(name = "PUBLICATION_TYP")
    private String publicationTyp;

    @Column(name = "IND_INCORR_RECP_DELETED")
    private String indIncorrRecpDeleted;

    @Column(name = "CORR_FILE_SEQ")
    private String corrFileSeq;

    @Column(name = "CORR_FILE_TYP")
    private String corrFileTyp;

    @Column(name = "CORR_FILE_SER")
    private Integer corrFileSer;

    @Column(name = "CORR_FILE_NBR")
    private Integer corrFileNbr;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false)
    })
    @IndexedEmbedded
    private List<IpDocFiles> ipDocFilesCollection;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", updatable = false),
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", updatable = false)}
    )
    private IpDoc ipDoc;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_NBR1", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SEQ1", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP1", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER1", referencedColumnName = "FILE_SER", insertable = false, updatable = false)
    })
    private List<IpFileRelationship> ipFileRelationships1;

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", insertable = false, updatable = false)
    })
    private List<IpFileRecordal> ipFileRecordals;

    @OneToMany(cascade =CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "FILE_NBR2", referencedColumnName = "FILE_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SEQ2", referencedColumnName = "FILE_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_TYP2", referencedColumnName = "FILE_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_SER2", referencedColumnName = "FILE_SER", insertable = false, updatable = false)
    })
    private List<IpFileRelationship> ipFileRelationships2;

    @ManyToOne
    @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", insertable = false, updatable = false)
    private CfFileType cfFileType;

    @OneToOne
    @JoinColumn(name = "PROC_NBR", referencedColumnName = "PROC_NBR", insertable = false, updatable = false)
    @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)
    @IndexedEmbedded(prefix = "", includePaths = {"proc.pk", "statusCode"})
    private IpProcSimple ipProcSimple;

}
