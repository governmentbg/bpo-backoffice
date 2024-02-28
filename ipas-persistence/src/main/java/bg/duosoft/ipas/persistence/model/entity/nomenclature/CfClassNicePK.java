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
public class CfClassNicePK implements Serializable {

    @Column(name = "NICE_CLASS_CODE")
    private Long niceClassCode;

    @Column(name = "NICE_CLASS_EDITION")
    private Long niceClassEdition;

    @Column(name = "NICE_CLASS_VERSION")
    private String niceClassVersion;
}
