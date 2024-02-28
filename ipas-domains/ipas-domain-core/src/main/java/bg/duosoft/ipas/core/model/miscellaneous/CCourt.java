package bg.duosoft.ipas.core.model.miscellaneous;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CCourt implements Serializable {

    private Integer id;
    private String alias;
    private String name;
    private String description;

}
