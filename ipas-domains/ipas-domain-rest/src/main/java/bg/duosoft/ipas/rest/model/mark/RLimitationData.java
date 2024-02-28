package bg.duosoft.ipas.rest.model.mark;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RLimitationData implements Serializable {
	private String disclaimer;
	private String byConsent;
	private String regulations;
	private String disclaimerInOtherLang;
}

