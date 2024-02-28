package bg.duosoft.ipas.rest.model.reception;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RReceptionEuPatent implements Serializable {
	private String userdocType;
	private Integer objectNumber;
}

