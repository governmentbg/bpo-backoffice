package bg.duosoft.ipas.persistence.model.entity.action;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IpActionLogDeletesPK implements Serializable {
    private String procTyp;
    private Integer procNbr;
    private Integer logActionNbr;
}
