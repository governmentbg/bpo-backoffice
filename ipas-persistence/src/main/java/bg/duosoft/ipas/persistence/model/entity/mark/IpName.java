package bg.duosoft.ipas.persistence.model.entity.mark;

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
@Table(name = "IP_NAME", schema = "IPASPROD")
@Cacheable(value = false)
public class IpName implements Serializable {

  @Column(name = "ROW_VERSION")
  private Integer rowVersion;

  @Id
  @Column(name = "MARK_CODE")
  private Integer markCode;

  @Column(name = "MARK_NAME")
  private String markName;

  @Column(name = "MAP_DENOMINACION")
  private String mapDenomination;

  @Column(name = "MARK_NAME_LANG2")
  private String markNameLang2;

}
