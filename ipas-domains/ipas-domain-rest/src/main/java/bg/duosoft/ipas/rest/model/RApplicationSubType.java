package bg.duosoft.ipas.rest.model;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RApplicationSubType implements Serializable {
	private String applicationType;
	private String applicationSubType;
	private String applicationSubTypeName;
}

