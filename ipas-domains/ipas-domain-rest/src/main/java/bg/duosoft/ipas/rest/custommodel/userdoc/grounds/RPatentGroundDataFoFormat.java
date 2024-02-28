package bg.duosoft.ipas.rest.custommodel.userdoc.grounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPatentGroundDataFoFormat {
    private Boolean partialInvalidity;
}
