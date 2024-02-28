package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RClaim implements Serializable {
	private Long claimNbr;
	private String claimDescription;
	private String claimEnglishDescription;
}

