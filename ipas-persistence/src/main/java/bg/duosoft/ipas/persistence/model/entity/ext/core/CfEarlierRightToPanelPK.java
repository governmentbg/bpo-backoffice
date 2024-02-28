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
public class CfEarlierRightToPanelPK implements Serializable {
    @Column(name = "earlier_right_type_id")
    private Integer earlierRightTypeId;

    @Column(name = "PANEL")
    private String panel;
}
