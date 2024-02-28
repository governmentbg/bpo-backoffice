package bg.duosoft.ipas.rest.model.userdoc.grounds;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RSingleDesignGroundData implements Serializable {
	private RPatent singleDesign;
}

