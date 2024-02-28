package bg.duosoft.ipas.persistence.model.entity.vw;

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
public class VwSelectNextProcessActionsId implements Serializable {

    @Column(name = "ACTION_TYP")
    private String actionTyp;

    @Column(name = "PROC_TYP")
    private String procTyp;

    @Column(name = "INITIAL_STATUS_CODE")
    private String initialStatusCode;

}
