package bg.duosoft.ipas.core.model.userdoc.grounds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPatentGroundData implements Serializable {
    private Boolean partialInvalidity;
}
