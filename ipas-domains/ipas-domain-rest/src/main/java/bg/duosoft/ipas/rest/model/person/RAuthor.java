package bg.duosoft.ipas.rest.model.person;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RAuthor implements Serializable {
	private Long authorSeq;
	private RPerson person;
}

