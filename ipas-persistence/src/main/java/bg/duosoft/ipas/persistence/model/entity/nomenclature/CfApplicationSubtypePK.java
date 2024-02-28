package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class CfApplicationSubtypePK implements Serializable {

  @Column(name = "APPL_TYP")
  private String applTyp;

  @Column(name = "APPL_SUBTYP")
  private String applSubtyp;

}
