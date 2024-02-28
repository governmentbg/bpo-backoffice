package bg.duosoft.ipas.rest.custommodel.userdoc.grounds;

import bg.duosoft.ipas.rest.model.patent.RPatent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RSingleDesignGroundDataFoFormat {
    private RPatent singleDesign;
}
