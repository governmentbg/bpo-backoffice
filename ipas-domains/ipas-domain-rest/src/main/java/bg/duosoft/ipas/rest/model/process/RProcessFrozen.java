package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RProcessFrozen implements Serializable {
	private String frozenProcessDescription;
	private Boolean indContinueWhenEnd;
	private Boolean indNoOffidoc;
	private RProcessId frozenProcessId;
}

