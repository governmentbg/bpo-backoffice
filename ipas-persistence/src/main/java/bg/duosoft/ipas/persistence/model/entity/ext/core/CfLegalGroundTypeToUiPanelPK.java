package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CfLegalGroundTypeToUiPanelPK implements Serializable {

    @Column(name = "legal_ground_type_id")
    private Integer legalGroundTypeId;

    @Column(name = "PANEL")
    private String panel;



}
