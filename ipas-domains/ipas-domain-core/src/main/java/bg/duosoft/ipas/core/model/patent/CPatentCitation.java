package bg.duosoft.ipas.core.model.patent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPatentCitation implements Serializable {
    private Integer refNumber;
    private String refDescription;
    private String refClaims;
}
