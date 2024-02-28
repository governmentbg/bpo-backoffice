package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CfClassCpcPK implements Serializable {

    @Column(name = "CPC_EDITION_CODE")
    private String cpcEditionCode;

    @Column(name = "CPC_SECTION_CODE")
    private String cpcSectionCode;

    @Column(name = "CPC_CLASS_CODE")
    private String cpcClassCode;

    @Column(name = "CPC_SUBCLASS_CODE")
    private String cpcSubclassCode;

    @Column(name = "CPC_GROUP_CODE")
    private String cpcGroupCode;

    @Column(name = "CPC_SUBGROUP_CODE")
    private String cpcSubgroupCode;

}
