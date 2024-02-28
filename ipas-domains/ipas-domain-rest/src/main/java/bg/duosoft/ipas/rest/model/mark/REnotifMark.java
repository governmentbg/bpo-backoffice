package bg.duosoft.ipas.rest.model.mark;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class REnotifMark implements Serializable {
	private Integer id;
	private String transaction;
	private String transcationType;
	private String originalLanguage;
	private String originalCountry;
	private String basicRegistrationNumber;
	private String designationType;
	private REnotif enotif;
}

