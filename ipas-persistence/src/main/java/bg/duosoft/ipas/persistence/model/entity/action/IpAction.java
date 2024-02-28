package bg.duosoft.ipas.persistence.model.entity.action;

import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfActionType;
import bg.duosoft.ipas.persistence.model.entity.nomenclature.CfStatus;
import bg.duosoft.ipas.persistence.model.entity.offidoc.IpOffidoc;
import bg.duosoft.ipas.persistence.model.entity.process.IpProc;
import bg.duosoft.ipas.persistence.model.entity.user.IpUser;
import bg.duosoft.ipas.persistence.model.entity.vw.VwJournal;
import bg.duosoft.ipas.util.search.IpActionBridge;
import bg.duosoft.ipas.util.search.IpProcBridge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "IP_ACTION", schema = "IPASPROD")
@Indexed
@Cacheable(value = false)
public class IpAction implements Serializable {

  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  @EmbeddedId
  @Field(name = "proc.pk", index = Index.YES, analyze = Analyze.NO, store = Store.YES, bridge = @FieldBridge(impl = IpProcBridge.class))
  @SortableField(forField = "proc.pk")
  @FieldBridge(impl = IpActionBridge.class)
  @IndexedEmbedded
  private IpActionPK pk;

  @ManyToOne
  @JoinColumn(name = "ACTION_TYP", referencedColumnName = "ACTION_TYP")
  @IndexedEmbedded(prefix = "action")
  private CfActionType actionTyp;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "ACTION_DATE")
  @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
  @SortableField
  @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
  private Date actionDate;

  @ManyToOne
  @JoinColumn(name = "CAPTURE_USER_ID", referencedColumnName = "USER_ID")
  @IndexedEmbedded
  private IpUser captureUser;

  @Column(name = "AUTHORISING_USER_ID")
  private Integer authorisingUserId;

  @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH})
  @JoinColumn(name = "JOURNAL_CODE", referencedColumnName = "JOURNAL_CODE")
  private IpJournal journal;

  @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH})
  @JoinColumns({
          @JoinColumn(name = "OFFIDOC_ORI", referencedColumnName = "OFFIDOC_ORI"),
          @JoinColumn(name = "OFFIDOC_SER", referencedColumnName = "OFFIDOC_SER"),
          @JoinColumn(name = "OFFIDOC_NBR", referencedColumnName = "OFFIDOC_NBR"),
  })
  private IpOffidoc ipOffidoc;

  @Column(name = "ACTION_NOTES")
  private String actionNotes;

  @Column(name = "NOTES1")
  private String notes1;

  @Column(name = "NOTES2")
  private String notes2;

  @Column(name = "NOTES3")
  private String notes3;

  @Column(name = "NOTES4")
  private String notes4;

  @Column(name = "NOTES5")
  private String notes5;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "PRIOR_STATUS_DATE")
  @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
  @SortableField
  @DateBridge(resolution = Resolution.MINUTE, encoding = EncodingType.STRING)
  private Date priorStatusDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "PRIOR_DUE_DATE")
  private Date priorDueDate;

  @ManyToOne
  @JoinColumn(name = "PRIOR_RESPONSIBLE_USER_ID", referencedColumnName = "USER_ID")
  private IpUser priorResponsibleUser;

  @Column(name = "IND_SIGNATURE_PENDING")
  private String indSignaturePending;

  @Column(name = "IND_CHANGES_STATUS")
  private String indChangesStatus;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CAPTURE_DATE")
  private Date captureDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "SIGNATURE_DATE")
  private Date signatureDate;

  @Column(name = "IND_CANCELLED")
  private String indCancelled;

  @Column(name = "IND_PUBLICATION_PROCESSED")
  private String indPublicationProcessed;

  @Column(name = "IND_JOURNAL_LINKAGE_INVALID")
  private String indJournalLinkageInvalid;

  @Column(name = "JOURNAL_LINKAGE_VALID_DATE")
  private Date journalLinkageValidDate;

  @Column(name = "ACTION_SEQ_SER")
  private Integer actionSeqSer;

  @Column(name = "ACTION_SEQ_NBR")
  private Integer actionSeqNbr;

  @Column(name = "ACTION_SEQ_TYP")
  private String actionSeqTyp;

  @Column(name = "IND_DELETED")
  private String indDeleted;

  @Column(name = "CHILD_ACTION_NBR")
  private Integer childActionNbr;

  @Column(name = "INDCHK1")
  private String indchk1;

  @Column(name = "INDCHK2")
  private String indchk2;

  @Column(name = "INDCHK3")
  private String indchk3;

  @Column(name = "INDCHK4")
  private String indchk4;

  @Column(name = "INDCHK5")
  private String indchk5;

  @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
  @JoinColumns({
          @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false),
          @JoinColumn(name = "PROC_NBR", referencedColumnName = "PROC_NBR", insertable = false, updatable = false)
  })
  private IpProc ipProc;

  @ManyToOne
  @JoinColumns({
          @JoinColumn(name = "NEW_STATUS_CODE", referencedColumnName = "STATUS_CODE", insertable = false, updatable = false),
          @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)})
  @IndexedEmbedded
  private CfStatus newStatusCode;

  @ManyToOne
  @JoinColumns({
          @JoinColumn(name = "PRIOR_STATUS_CODE", referencedColumnName = "STATUS_CODE", insertable = false, updatable = false),
          @JoinColumn(name = "PROC_TYP", referencedColumnName = "PROC_TYP", insertable = false, updatable = false)})
  @IndexedEmbedded
  private CfStatus priorStatusCode;

  @ManyToOne
  @JoinColumn(name = "RESPONSIBLE_USER_ID", referencedColumnName = "USER_ID")
  @IndexedEmbedded
  private IpUser responsibleUser;

  @OneToOne
  @JoinColumns({
          @JoinColumn(name = "PROC_TYP",   referencedColumnName = "PROC_TYP",   insertable = false, updatable = false),
          @JoinColumn(name = "PROC_NBR",   referencedColumnName = "PROC_NBR",   insertable = false, updatable = false),
          @JoinColumn(name = "ACTION_NBR", referencedColumnName = "ACTION_NBR", insertable = false, updatable = false)
  })
  @IndexedEmbedded
  private VwJournal vwJournal;

}
