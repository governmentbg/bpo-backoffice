package bg.duosoft.ipas.core.model.userdoc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CUserdocPatentData  implements Serializable {
    private String titleBg;
    private Integer descriptionPagesCount;
    private Integer claimsCount;
    private Integer drawingsCount;
}
