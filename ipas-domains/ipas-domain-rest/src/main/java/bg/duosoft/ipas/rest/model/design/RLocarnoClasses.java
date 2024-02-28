package bg.duosoft.ipas.rest.model.design;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RLocarnoClasses implements Serializable {
	private String locarnoClassCode;
	private String locarnoEditionCode;
	private String locarnoName;
}

