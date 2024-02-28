package bg.duosoft.ipas.core.model.person;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CPersonAbdocsSync implements Serializable {
    private Integer id;
    private Integer personNbr;
    private Integer addrNbr;
    private Date insertedAt;
    private Date processedAt;
    private Boolean indSync;
}
