package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CfLawApplicationSubtypePK implements Serializable {
    @Column(name = "LAW_CODE")
    private Integer lawCode;
    @Column(name = "APPL_TYP")
    private String applTyp;
    @Column(name = "APPL_SUBTYP")
    private String applSubtyp;
}
