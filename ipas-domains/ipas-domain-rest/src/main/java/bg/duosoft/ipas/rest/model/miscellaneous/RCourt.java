package bg.duosoft.ipas.rest.model.miscellaneous;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RCourt implements Serializable {
	private Integer id;
	private String alias;
	private String name;
	private String description;
}

