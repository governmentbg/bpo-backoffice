package bg.duosoft.ipas.persistence.model.entity.action;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IpActionOptionsPK implements Serializable {
    private String procTyp;
    private Integer procNbr;
    private Integer actionNbr;
    private String listCode;
    private String optionNbr;
}
