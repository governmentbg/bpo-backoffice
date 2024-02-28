package bg.duosoft.ipas.persistence.model.entity.person;

import bg.duosoft.ipas.persistence.model.entity.ext.agent.ExtendedPartnership;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "IP_PERSON", schema = "IPASPROD")
@Cacheable(value = false)
@AnalyzerDef(name = "FullTextAnalyzer",
        tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                        @Parameter(name = "pattern", value = "^\\s*"),
                        @Parameter(name = "replacement", value = ""),
                        @Parameter(name = "replace", value = "all")
                }),
                @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                        @Parameter(name = "pattern", value = "[\\„\\\"\\']"),
                        @Parameter(name = "replacement", value = ""),
                        @Parameter(name = "replace", value = "all")
                }),
                @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                        @Parameter(name = "pattern", value = "\\s{2,}"),
                        @Parameter(name = "replacement", value = " "),
                        @Parameter(name = "replace", value = "all")
                })
        })
@AnalyzerDef(name = "WordAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                        @Parameter(name = "pattern", value = "^\\s*"),
                        @Parameter(name = "replacement", value = ""),
                        @Parameter(name = "replace", value = "all")
                }),
                @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                        @Parameter(name = "pattern", value = "[\\„\\\"\\']"),
                        @Parameter(name = "replacement", value = ""),
                        @Parameter(name = "replace", value = "all")
                }),
                @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
                        @Parameter(name = "pattern", value = "\\s{2,}"),
                        @Parameter(name = "replacement", value = " "),
                        @Parameter(name = "replace", value = "all")
                })
        })
@Audited
@AuditTable(value = "IP_PERSON", schema = "AUDIT")
public class IpPerson implements Serializable {

    @Column(name = "ROW_VERSION")
    private Integer rowVersion;

    @Id
    @Column(name = "PERSON_NBR")
    @Field(name = "person_nbr", index = Index.YES, store = Store.YES)
    @SortableField(forField = "person_nbr")
    @NumericField(forField = "person_nbr")
    @FieldBridge(impl = IntegerBridge.class)
    private Integer personNbr;

    @Column(name = "PERSON_NAME")
    @Field(name = "personNameExact", analyze = Analyze.NO, index = Index.YES, store = Store.YES, indexNullAs = "", norms = Norms.NO)
    @Field(analyze = Analyze.YES, index = Index.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(definition = "WordAnalyzer"))
    @Field(name = "personNameCustom", analyze = Analyze.YES, index = Index.YES, store = Store.YES, indexNullAs = "", analyzer = @Analyzer(definition = "FullTextAnalyzer"))
    @Field(name = "personNameSort", analyze = Analyze.NO, index = Index.NO, store = Store.YES, normalizer = @Normalizer(definition = "sortNormalizer"), indexNullAs = "")
    @SortableField(forField = "personNameSort")
    private String personName;

    @Column(name = "PERSON_WCODE")
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES, indexNullAs = "")
    private String personWcode;

    @Column(name = "NATIONALITY_COUNTRY_CODE")
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES, norms = Norms.NO)
    private String nationalityCountryCode;

    @Column(name = "LEGAL_NATURE")
    private String legalNature;

    @Column(name = "TELEPHONE")
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES)
    private String telephone;

    @Column(name = "EMAIL")
    @Field(analyze = Analyze.YES, index = Index.YES, store = Store.YES, analyzer = @Analyzer(impl = UAX29URLEmailAnalyzer.class))
    @Field(name = "emailExact", analyze = Analyze.NO, index = Index.YES, store = Store.YES, norms = Norms.NO)
    private String email;

    @Column(name = "PERSON_GROUP_NBR")
    private String personGroupNbr;

    @Column(name = "GRAL_PERSON_ID_TYP")
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES, indexNullAs = "")
    @SortableField
    private String gralPersonIdTyp;

    @Column(name = "GRAL_PERSON_ID_NBR")
    private Integer gralPersonIdNbr;

    @Column(name = "GRAL_PERSON_ID_TXT")
    private String gralPersonIdTxt;

    @Column(name = "INDI_PERSON_ID_TYP")
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES, norms = Norms.NO)
    private String indiPersonIdTyp;

    @Column(name = "INDI_PERSON_ID_NBR")
    private Integer indiPersonIdNbr;

    @Column(name = "INDI_PERSON_ID_TXT")
    @Field(analyze = Analyze.NO, index = Index.YES, store = Store.YES, norms = Norms.NO)
    private String indiPersonIdTxt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COMPANY_REGISTER_DATE")
    private Date companyRegisterDate;

    @Column(name = "COMPANY_REGISTER_NBR")
    private String companyRegisterNbr;

    @Column(name = "PERSON_NAME_LANG2")
    private String personNameLang2;

    @Column(name = "LEGAL_NATURE_LANG2")
    private String legalNatureLang2;

    @OneToMany(mappedBy = "ipPerson", cascade = CascadeType.ALL)
    private List<IpPersonAddresses> addresses;

    @OneToOne
    @JoinColumn(name = "AGENT_CODE", referencedColumnName = "AGENT_CODE")
    @IndexedEmbedded
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private IpAgent ipAgent;

    @OneToOne
    @JoinColumn(name = "PERSON_NBR", referencedColumnName = "PERSON_NBR", updatable = false, insertable = false)
    @IndexedEmbedded
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ExtendedPartnership extendedPartnership;

}
