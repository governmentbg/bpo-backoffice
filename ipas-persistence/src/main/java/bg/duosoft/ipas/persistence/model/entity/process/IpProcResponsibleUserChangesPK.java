package bg.duosoft.ipas.persistence.model.entity.process;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IpProcResponsibleUserChangesPK implements Serializable {
    private String procTyp;
    private Integer procNbr;
    private Integer changeNbr;
}
