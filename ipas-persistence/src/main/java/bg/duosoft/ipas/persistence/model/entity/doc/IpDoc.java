package bg.duosoft.ipas.persistence.model.entity.doc;

import bg.duosoft.ipas.persistence.model.entity.dailylog.IpDailyLog;
import bg.duosoft.ipas.persistence.model.entity.efiling.IpUserodocEFilingData;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpUserdocApprovedData;
import bg.duosoft.ipas.persistence.model.entity.file.IpFile;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfDocSequence;
import bg.duosoft.ipas.persistence.model.entity.patent_data.IpUserdocPatentData;
import bg.duosoft.ipas.persistence.model.entity.process.IpProcSimple;
import bg.duosoft.ipas.persistence.model.entity.userdoc.IpUserdoc;
import bg.duosoft.ipas.util.search.IpDocPKBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Indexed
@Table(name = "IP_DOC", schema = "IPASPROD")
@Cacheable(value = false)
@AnalyzerDef(name = "ExternalSystemIdAnalyzer",
        tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
        })
public class IpDoc implements Serializable {
    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @EmbeddedId
    @FieldBridge(impl = IpDocPKBridge.class)
    @SortableField
    private IpDocPK pk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FILING_DATE")
    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
    @Field(name = "filingDateSort", index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    @SortableField(forField = "filingDateSort")
    private Date filingDate;

    @Column(name = "RECEPTION_WCODE")
    private String receptionWcode;

    @Column(name = "EXTERNAL_OFFICE_CODE")
    private Long externalOfficeCode;

    @Column(name = "EXTERNAL_OFFICE_FILING_DATE")
    private Date externalOfficeFilingDate;

    @Column(name = "EXTERNAL_SYSTEM_ID")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "ExternalSystemIdAnalyzer"))
    @SortableField
    private String externalSystemId;

    @Column(name = "IND_NOT_ALL_FILES_CAPTURED_YET")
    private String indNotAllFilesCapturedYet;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "APPL_TYP")
    private String applTyp;

    @Column(name = "APPL_SUBTYP")
    private String applSubtyp;

    @Column(name = "AUXIL1_NBR")
    private Integer auxil1Nbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECEPTION_DATE")
    private Date receptionDate;

    @ManyToOne
    @JoinColumn(name = "DOC_SEQ_TYP", referencedColumnName = "DOC_SEQ_TYP")
    @IndexedEmbedded
    private CfDocSequence docSeqTyp;

    @Column(name = "DOC_SEQ_NBR")
    @Field(index= Index.YES, analyze = Analyze.NO, store = Store.YES)
    @NumericField(forField = "docSeqNbr")
    private Integer docSeqNbr;

    @Column(name = "DOC_SEQ_SERIES")
    private Integer docSeqSeries;

    @Column(name = "RECEPTION_USER_ID")
    private Integer receptionUserId;

    @Column(name = "IND_FAX_RECEPTION")
    private String indFaxReception;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAX_RECEP_DATE")
    private Date faxRecepDate;

    @Column(name = "ACK_OFFIDOC_ORI")
    private String ackOffidocOri;

    @Column(name = "ACK_OFFIDOC_SER")
    private Integer ackOffidocSer;

    @Column(name = "ACK_OFFIDOC_NBR")
    private Integer ackOffidocNbr;

    @Column(name = "APPL_DOC_ORI")
    private String applDocOri;

    @Column(name = "APPL_LOG_TYP")
    private String applLogTyp;

    @Column(name = "APPL_DOC_SER")
    private Integer applDocSer;

    @Column(name = "APPL_DOC_NBR")
    private Integer applDocNbr;

    @Column(name = "INCOMPLETE_APPL_DOC_ORI")
    private String incompleteApplDocOri;

    @Column(name = "INCOMPLETE_APPL_LOG_TYP")
    private String incompleteApplLogTyp;

    @Column(name = "INCOMPLETE_APPL_DOC_SER")
    private Integer incompleteApplDocSer;

    @Column(name = "INCOMPLETE_APPL_DOC_NBR")
    private Integer incompleteApplDocNbr;

    @Column(name = "IND_ORIGINAL_FAX")
    private String indOriginalFax;

    @Column(name = "RESPONDED_DOC_ORI")
    private String respondedDocOri;

    @Column(name = "RESPONDED_LOG_TYP")
    private String respondedLogTyp;

    @Column(name = "RESPONDED_DOC_SER")
    private Integer respondedDocSer;

    @Column(name = "RESPONDED_DOC_NBR")
    private Integer respondedDocNbr;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MAIL_NOTIF_DATE")
    private Date mailNotifDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NOTIFICATION_DATE")
    private Date notificationDate;

    @Column(name = "IND_RESPONSE_REQ")
    private String indResponseReq;

    @Column(name = "IND_SYSTEM")
    private String indSystem;

    @Column(name = "OFFIDOC_ORI")
    private String offidocOri;

    @Column(name = "OFFIDOC_SER")
    private Integer offidocSer;

    @Column(name = "OFFIDOC_NBR")
    private Integer offidocNbr;

    @Column(name = "DOC_DESCRIPTION")
    private String docDescription;

    @Column(name = "DOC_REF")
    private Integer docRef;

    @Column(name = "OFFICE_DIVISION_CODE")
    private String officeDivisionCode;

    @Column(name = "OFFICE_DEPARTMENT_CODE")
    private String officeDepartmentCode;

    @Column(name = "OFFICE_SECTION_CODE")
    private String officeSectionCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ORIG_RECEP_DATE")
    private Date origRecepDate;

    @Column(name = "IND_SPECIFIC_EDOC")
    private String indSpecificEdoc;

    @Column(name = "IND_INTERFACE_EDOC")
    private String indInterfaceEdoc;

    @Column(name = "CREATED_EFOLDER_ID")
    private Integer createdEfolderId;

    @Column(name = "EDOC_ID")
    private Integer edocId;

    @Column(name = "DATA_CODE_TYP1")
    private String dataCodeTyp1;

    @Column(name = "DATA_CODE_ID1")
    private String dataCodeId1;

    @Column(name = "DATA_CODE_TYP2")
    private String dataCodeTyp2;

    @Column(name = "DATA_CODE_ID2")
    private String dataCodeId2;

    @Column(name = "DATA_CODE_TYP3")
    private String dataCodeTyp3;

    @Column(name = "DATA_CODE_ID3")
    private String dataCodeId3;

    @Column(name = "DATA_CODE_TYP4")
    private String dataCodeTyp4;

    @Column(name = "DATA_CODE_ID4")
    private String dataCodeId4;

    @Column(name = "DATA_CODE_TYP5")
    private String dataCodeTyp5;

    @Column(name = "DATA_CODE_ID5")
    private String dataCodeId5;

    @Column(name = "DATA_TEXT1")
    private String dataText1;

    @Column(name = "DATA_TEXT2")
    private String dataText2;

    @Column(name = "DATA_TEXT3")
    private String dataText3;

    @Column(name = "DATA_TEXT4")
    private String dataText4;

    @Column(name = "DATA_TEXT5")
    private String dataText5;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_DATE1")
    private Date dataDate1;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_DATE2")
    private Date dataDate2;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_DATE3")
    private Date dataDate3;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_DATE4")
    private Date dataDate4;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_DATE5")
    private Date dataDate5;

    @Column(name = "DATA_NBR1")
    private Integer dataNbr1;

    @Column(name = "DATA_NBR2")
    private Integer dataNbr2;

    @Column(name = "DATA_NBR3")
    private Integer dataNbr3;

    @Column(name = "DATA_NBR4")
    private Integer dataNbr4;

    @Column(name = "DATA_NBR5")
    private Integer dataNbr5;

    @Column(name = "DATA_FLAG1")
    private String dataFlag1;

    @Column(name = "DATA_FLAG2")
    private String dataFlag2;

    @Column(name = "DATA_FLAG3")
    private String dataFlag3;

    @Column(name = "DATA_FLAG4")
    private String dataFlag4;

    @Column(name = "DATA_FLAG5")
    private String dataFlag5;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "FILE_SEQ", referencedColumnName = "FILE_SEQ", updatable = false),
            @JoinColumn(name = "FILE_TYP", referencedColumnName = "FILE_TYP", updatable = false),
            @JoinColumn(name = "FILE_SER", referencedColumnName = "FILE_SER", updatable = false),
            @JoinColumn(name = "FILE_NBR", referencedColumnName = "FILE_NBR", updatable = false),
    })
    private IpFile file;



    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(column = @JoinColumn(name = "DAILY_LOG_DATE", referencedColumnName = "DAILY_LOG_DATE")),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "DOC_ORI", referencedColumnName = "DOC_ORI")),
            @JoinColumnOrFormula(formula = @JoinFormula(value = "DOC_LOG", referencedColumnName = "DOC_LOG"))
    })
    private IpDailyLog ipDailyLog;

    @OneToOne(mappedBy = "ipDoc", cascade = {CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumns({
//            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
//            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
//            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
//            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false)
//    })
    private IpUserdoc ipUserdoc;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false)
    })
    @IndexedEmbedded
    private IpProcSimple proc;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private IpUserodocEFilingData userdocEFilingData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
        @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
        @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
        @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private IpUserdocApprovedData ipUserdocApprovedData;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "DOC_ORI", referencedColumnName = "DOC_ORI", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_LOG", referencedColumnName = "DOC_LOG", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_SER", referencedColumnName = "DOC_SER", insertable = false, updatable = false),
            @JoinColumn(name = "DOC_NBR", referencedColumnName = "DOC_NBR", insertable = false, updatable = false)
    })
    private IpUserdocPatentData patentData;

}
