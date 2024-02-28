package bg.duosoft.ipas.rest.model.patent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RPatentCitation implements Serializable {
    private Integer refNumber;
    private String refDescription;
    private String refClaims;
}
