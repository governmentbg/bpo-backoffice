package bg.duosoft.ipas.persistence.model.entity.ext.core;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CfUserdocExtraDataToUiPanelPK implements Serializable {

    @Column(name = "CODE")
    private String code;

    @Column(name = "PANEL")
    private String panel;
}
