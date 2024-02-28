package bg.duosoft.ipas.rest.model.miscellaneous;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RStatus implements Serializable {
	private String statusName;
	private Boolean indPending;
	private Boolean indResponsibleRequired;
	private RStatusId statusId;
	private Integer triggerActivityWcode;
	private String processResultType;
}

