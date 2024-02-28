package bg.duosoft.ipas.persistence.model.entity.process;

import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.util.search.IpProcBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "IP_PROC", schema = "IPASPROD")
@Cacheable(value = false)
public class IpProcSimple implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @Field(name = "proc.pk", index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField(forField = "proc.pk")
    @FieldBridge(impl = IpProcBridge.class)
    private IpProcPK pk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;

//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumns({
//            @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false),
//            @JoinColumn(name = "STATUS_CODE", referencedColumnName = "STATUS_CODE", insertable = false, updatable = false),
//    })
//    private CfStatus statusCode;

    @Column(name = "STATUS_CODE")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    private String statusCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATUS_DATE")
    private Date statusDate;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "EXPIRATION_DATE")
//    private Date expirationDate;

//    @Column(name = "IND_FREEZING_JUST_ENDED")
//    private String indFreezingJustEnded;

//    @Column(name = "MANUAL_PROC_DESCRIPTION")
//    private String manualProcDescription;


    @Column(name = "FILE_PROC_TYP")
    private String fileProcTyp;

    @Column(name = "FILE_PROC_NBR")
    private Integer fileProcNbr;

    @Column(name = "DOC_ORI")
    private String docOri;

    @Column(name = "DOC_LOG")
    private String docLog;

    @Column(name = "DOC_SER")
    private Integer docSer;

    @Column(name = "DOC_NBR")
    private Integer docNbr;

    @Column(name = "FILE_SEQ")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String fileSeq;

    @Column(name = "FILE_TYP")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    private String fileTyp;

    @Column(name = "FILE_SER")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @NumericField
    private Integer fileSer;

    @Column(name = "FILE_NBR")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @NumericField(forField = "fileNbr")
    @Field(name = "fileNbrStr",
            index= Index.YES,
            analyze = Analyze.NO,
            store = Store.YES,
            bridge = @FieldBridge(impl = IntegerBridge.class),
            normalizer = @Normalizer(definition = "sortNormalizer"))
    private Integer fileNbr;

//    @Column(name = "DROP1")
//    private Integer drop1;

//    @Column(name = "DROP2")
//    private String drop2;

//    @Column(name = "MANUAL_PROC_REF")
//    private Integer manualProcRef;

//    @Column(name = "END_FREEZE_FLAG")
//    private String endFreezeFlag;

//    @Column(name = "IND_SIGNATURE_PENDING")
//    private String indSignaturePending;

//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)
//    private CfProcessType processType;

//    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumns({
//            @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false),
//            @JoinColumn(name = "STATUS_CODE", referencedColumnName = "STATUS_CODE", insertable = false, updatable = false),
//            @JoinColumn(name = "SUB_STATUS_CODE", referencedColumnName = "SUB_STATUS_CODE", insertable = false, updatable = false)
//    })
//    private CfSubStatus subStatusCode;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "RESPONSIBLE_USER_ID", referencedColumnName = "USER_ID")
    private IpUser responsibleUser;

    @EqualsAndHashCode.Exclude
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "FILE_PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false),
            @JoinColumn(name = "FILE_PROC_NBR", referencedColumnName = "PROC_NBR", insertable = false, updatable = false)
    })
    @IndexedEmbedded(includePaths = {"fileNbr"}, depth = 1)
    private IpProcSimple subProc;

    @Column(name = "USERDOC_TYP")
    @Field(analyze = Analyze.NO, store = Store.YES)
    @SortableField
    private String userdocTyp;
}
