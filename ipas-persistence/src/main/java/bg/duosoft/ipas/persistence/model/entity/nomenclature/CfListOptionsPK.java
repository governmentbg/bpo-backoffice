package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CfListOptionsPK implements Serializable {
    private String listCode;
    private String optionNbr;
}
