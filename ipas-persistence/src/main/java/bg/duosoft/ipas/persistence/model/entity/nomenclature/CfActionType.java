package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_ACTION_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfActionType implements Serializable {

  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  @Id
  @Column(name = "ACTION_TYP")
  @Field(name = "Typ", index= Index.YES, analyze = Analyze.NO, store = Store.YES)
  private String actionTyp;

  @Column(name = "ACTION_TYPE_NAME")
  private String actionTypeName;

  @Column(name = "AUTOMATIC_ACTION_WCODE")
  private Long automaticActionWcode;

  @ManyToOne
  @JoinColumn(name = "LIST_CODE", referencedColumnName = "LIST_CODE")
  private CfList list;

  @Column(name = "DUE_DATE_CALCULATION_WCODE")
  private Long dueDateCalculationWcode;

  @Column(name = "DUE_MONTHS")
  private Integer dueMonths;

  @Column(name = "DUE_WORKING_DAYS")
  private Integer dueWorkingDays;

  @Column(name = "JOURNAL_PUBLICATION_WCODE")
  private Long journalPublicationWcode;

  @Column(name = "NOTES1_PROMPT")
  private String notes1Prompt;

  @Column(name = "NOTES2_PROMPT")
  private String notes2Prompt;

  @Column(name = "NOTES3_PROMPT")
  private String notes3Prompt;

  @Column(name = "NOTES4_PROMPT")
  private String notes4Prompt;

  @Column(name = "NOTES5_PROMPT")
  private String notes5Prompt;

  @Column(name = "JOURNAL_WFILE")
  private String journalWfile;

  @Column(name = "IND_BRKDOWN_APPL_TYPE")
  private String indBrkdownApplType;

  @Column(name = "ORDER_BY_WCODE")
  private String orderByWcode;

  @Column(name = "IND_WORD_TITLE")
  private String indWordTitle;

  @Column(name = "IND_APPEAL")
  private String indAppeal;

  @Column(name = "IND_RESOLUTION")
  private String indResolution;

  @Column(name = "DUE_CALENDAR_DAYS")
  private Integer dueCalendarDays;

  @Column(name = "IND_BRKDOWN_APPL_SUBTYPE")
  private String indBrkdownApplSubtype;

  @Column(name = "IND_BRKDOWN_USERDOC_TYPE")
  private String indBrkdownUserdocType;

  @Column(name = "RESTRICT_LAW_CODE")
  private Integer restrictLawCode;

  @Column(name = "IND_SHOW_TO_PUBLIC")
  private String indShowToPublic;

  @Column(name = "RESTRICT_APPL_TYP")
  private String restrictApplTyp;

  @Column(name = "RESTRICT_APPL_SUBTYP")
  private String restrictApplSubtyp;

  @Column(name = "IND_ANNUITIES")
  private String indAnnuities;

  @Column(name = "XML_DESIGNER")
  private String xmlDesigner;

  @Column(name = "IND_SAME_SEQ_IN_GROUP")
  private String indSameSeqInGroup;

  @Column(name = "RESTRICT_FILE_TYP")
  private String restrictFileTyp;

  @Column(name = "IND_USED")
  private String indUsed;

  @Column(name = "IND_INACTIVE")
  private String indInactive;

  @Column(name = "PUBLICATION_NBR_WCODE")
  private String publicationNbrWcode;

  @Column(name = "PUBLICATION_CODE")
  private String publicationCode;

  @Column(name = "SQL_FIELD_CODE")
  private String sqlFieldCode;

  @Column(name = "TEXT1")
  private String text1;

  @Column(name = "TEXT5")
  private String text5;

  @Column(name = "IND_JOURNAL_AUTOASSIGN")
  private String indJournalAutoassign;

  @Column(name = "INDEX_CLASS_WFILE")
  private String indexClassWfile;

  @Column(name = "INDEX_OWNER_WFILE")
  private String indexOwnerWfile;

  @Column(name = "INDEX_FILE_WFILE")
  private String indexFileWfile;

  @Column(name = "INDEX_REF_WCODE")
  private String indexRefWcode;

  @Column(name = "PRIORITY_ACTION_WCODE")
  private String priorityActionWcode;

  @Column(name = "END_FREEZE_FLAG")
  private String endFreezeFlag;

  @Column(name = "GENERATED_BY_USERDOC_TYP")
  private String generatedByUserdocTyp;

  @Column(name = "action_seq_typ")
  private String actionSeqTyp;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "ACTION_TYPE_GROUP")
  private String actionTypeGroup;

  @Column(name = "EMAIL_GENERATION_WCODE")
  private Long emailGenerationWcode;

  @Column(name = "EMAIL_RECIPIENT_WCODE")
  private Long emailRecipientWcode;

  @Column(name = "EMAIL_RECIPIENT_FIELD")
  private String emailRecipientField;

  @Column(name = "EMAIL_TEMPLATE_TITLE")
  private String emailTemplateTitle;

  @Column(name = "EMAIL_TEMPLATE_BODY")
  private String emailTemplateBody;

  @Column(name = "EMAIL_TEMPLATE_ATTACHMENT")
  private String emailTemplateAttachment;

  @Column(name = "TEXT2")
  private String text2;

  @Column(name = "TEXT3")
  private String text3;

  @Column(name = "TEXT4")
  private String text4;

  @Column(name = "USERDOC_LIST_CODE_INCLUDE")
  private String userdocListCodeInclude;

  @Column(name = "USERDOC_LIST_CODE_EXCLUDE")
  private String userdocListCodeExclude;

  @Column(name = "FUNCTION_NAME")
  private String functionName;

  @Column(name = "IND_DELETE_ANYBODY")
  private String indDeleteAnybody;

  @Column(name = "IND_SECRET")
  private String indSecret;

  @Column(name = "MADRID_DECISION")
  private String madridDecision;

  @Column(name = "CHK1_PROMPT")
  private String chk1Prompt;

  @Column(name = "CHK2_PROMPT")
  private String chk2Prompt;

  @Column(name = "CHK3_PROMPT")
  private String chk3Prompt;

  @Column(name = "CHK4_PROMPT")
  private String chk4Prompt;

  @Column(name = "CHK5_PROMPT")
  private String chk5Prompt;

  @Column(name = "LIST_CODE2")
  private String listCode2;

  @Column(name = "LIST_CODE3")
  private String listCode3;

}
