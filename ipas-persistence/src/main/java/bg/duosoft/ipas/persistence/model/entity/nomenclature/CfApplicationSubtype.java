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
@Table(name = "CF_APPLICATION_SUBTYPE", schema = "IPASPROD")
@Cacheable(value = false)
public class CfApplicationSubtype implements Serializable {

  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  @EmbeddedId
  private CfApplicationSubtypePK pk;

  @Column(name = "APPL_SUBTYPE_NAME")
  private String applSubtypeName;

  @Column(name = "IND_RENEWAL")
  private String indRenewal;

  @Column(name = "IND_PCT")
  private String indPct;

  @Column(name = "MONTHS_RENEWAL_BEFORE_EXPIR")
  private Long monthsRenewalBeforeExpir;

  @Column(name = "MONTHS_RENEWAL_AFTER_EXPIR")
  private Long monthsRenewalAfterExpir;

  @Column(name = "XML_DESIGNER")
  private String xmlDesigner;

  @Column(name = "IND_USED")
  private String indUsed;

  @Column(name = "EDOC_TYP")
  private String edocTyp;

  @Column(name = "GENERIC_EDOC_TYP")
  private String genericEdocTyp;

  @ManyToOne
  @JoinColumn(name = "APPL_TYP", referencedColumnName = "APPL_TYP", insertable = false, updatable = false)
  private CfApplicationType cfApplicationType;

}
