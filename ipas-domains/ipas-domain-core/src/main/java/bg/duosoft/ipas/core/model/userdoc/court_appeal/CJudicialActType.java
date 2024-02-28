package bg.duosoft.ipas.core.model.userdoc.court_appeal;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CJudicialActType implements Serializable {
    private Integer id;
    private String actTypeName;
}
