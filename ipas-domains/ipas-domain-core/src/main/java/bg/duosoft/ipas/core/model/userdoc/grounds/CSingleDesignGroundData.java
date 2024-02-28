package bg.duosoft.ipas.core.model.userdoc.grounds;

import bg.duosoft.ipas.core.model.patent.CPatent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CSingleDesignGroundData  implements Serializable {
    private CPatent singleDesign;
}
