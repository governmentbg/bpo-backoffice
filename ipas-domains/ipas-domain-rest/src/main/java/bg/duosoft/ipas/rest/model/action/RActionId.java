package bg.duosoft.ipas.rest.model.action;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RActionId implements Serializable {
	private Integer actionNbr;
	private RProcessId processId;
}

