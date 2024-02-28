package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CF_APPLICATION_TYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfApplicationType implements Serializable {

  public CfApplicationType(String applTyp) {
    this.applTyp = applTyp;
  }

  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  @Id
  @Column(name = "APPL_TYP")
  private String applTyp;

  @Column(name = "APPL_TYPE_NAME")
  private String applTypeName;

  @Column(name = "CERTIFICATE_TITLE")
  private String certificateTitle;

  @Column(name = "FILE_TYP")
  private String fileTyp;

  @Column(name = "GENERATE_PROC_TYP")
  private String generateProcTyp;

  @Column(name = "TABLE_NAME")
  private String tableName;

  @Column(name = "PARIS_PRIORITY_MONTHS")
  private Long parisPriorityMonths;

  @Column(name = "IND_IPC")
  private String indIpc;

  @Column(name = "IND_LOCARNO")
  private String indLocarno;

  @Column(name = "EXPOSITION_PRIORITY_MONTHS")
  private Long expositionPriorityMonths;

  @Column(name = "XML_DESIGNER")
  private String xmlDesigner;

  @Column(name = "IND_INACTIVE")
  private String indInactive;

  @Column(name = "IND_VALID_IN_STATES")
  private String indValidInStates;

}
