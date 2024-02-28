package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.enums.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocPersonRole implements Serializable {
	private UserdocPersonRole role;
	private Boolean indTakeFromOwner;
	private Boolean indTakeFromRepresentative;
	private Boolean indAdditionalOffidocCorrespondent;
	private String name;
}

