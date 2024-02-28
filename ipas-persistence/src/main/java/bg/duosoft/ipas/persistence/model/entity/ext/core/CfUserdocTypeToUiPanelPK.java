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
public class CfUserdocTypeToUiPanelPK implements Serializable {

    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

    @Column(name = "PANEL")
    private String panel;

}
