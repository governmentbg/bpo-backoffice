package bg.duosoft.ipas.rest.model.userdoc.court_appeal;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RJudicialActType implements Serializable {
	private Integer id;
	private String actTypeName;
}

