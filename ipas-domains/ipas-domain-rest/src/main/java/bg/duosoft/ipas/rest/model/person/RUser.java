package bg.duosoft.ipas.rest.model.person;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUser implements Serializable {
	private String userName;
	private Integer userId;
	private Boolean indInactive;
}

