package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_STATUS", schema = "IPASPROD")
@Cacheable(value = false)
public class CfStatus implements Serializable {
  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  public CfStatus(CfStatusPK pk) {
    this.pk = pk;
  }
  @EmbeddedId
  @IndexedEmbedded
  private CfStatusPK pk;

  @Column(name = "STATUS_NAME")
  private String statusName;

  @Column(name = "IND_RESPONSIBLE_REQ")
  private String indResponsibleReq;

  @Column(name = "TRIGGER_ACTIVITY_WCODE")
  private Integer triggerActivityWcode;

  @Column(name = "IND_TRIGGER_END_FREEZE")
  private String indTriggerEndFreeze;

  @Column(name = "GENERATE_PROC_TYP")
  private String generateProcTyp;

  @Column(name = "SQL_STATEMENT")
  private String sqlStatement;

  @Column(name = "IND_RESPONSIBLE_EXAMINER")
  private String indResponsibleExaminer;

  @Column(name = "PROCESS_RESULT_TYP")
  private String processResultTyp;

  @Column(name = "XML_DESIGNER")
  private String xmlDesigner;

  @Column(name = "IND_USED")
  private String indUsed;

  @Column(name = "IND_EXCLUSIVE_RESPONSIBLE")
  private String indExclusiveResponsible;

  @Column(name = "IND_SIMILARITY_IGNORE")
  private String indSimilarityIgnore;

  @Column(name = "IND_DUMMY")
  private String indDummy;

  @Column(name = "RESPONSIBLE_GROUP_ID")
  private Integer responsibleGroupId;

  @Column(name = "STATUS_GROUP_CODE")
  private String statusGroupCode;

  @Column(name = "RESPONSIBLE_OFFICE_DIVI_CODE")
  private String responsibleOfficeDiviCode;

  @Column(name = "RESPONSIBLE_OFFICE_DEP_CODE")
  private String responsibleOfficeDepCode;

  @Column(name = "RESPONSIBLE_OFFICE_SECT_CODE")
  private String responsibleOfficeSectCode;

  @Column(name = "RESTRICT_END_FREEZE_FLAG")
  private String restrictEndFreezeFlag;

  @Column(name = "NOTES")
  private String notes;

  @Column(name = "TRIGGER_ACTIVITY_WCODE2")
  private Long triggerActivityWcode2;

  @Column(name = "TRIGGER_ACTIVITY_WCODE3")
  private Long triggerActivityWcode3;

  @Column(name = "TRIGGER_ACTIVITY_WCODE4")
  private Long triggerActivityWcode4;

  @Column(name = "TRIGGER_ACTIVITY_WCODE5")
  private Long triggerActivityWcode5;

  @Column(name = "IND_INACTIVE")
  private String indInactive;

  @Column(name = "EXTERNAL_INTERFACE_URL")
  private String externalInterfaceUrl;

  @Column(name = "IND_KEEP_RES_USER")
  private String indKeepResUser;

}
