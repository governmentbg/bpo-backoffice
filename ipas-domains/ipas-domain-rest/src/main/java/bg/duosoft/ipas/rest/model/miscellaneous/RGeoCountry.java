package bg.duosoft.ipas.rest.model.miscellaneous;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RGeoCountry implements Serializable {
	private String rowVersion;
	private String countryCode;
	private String countryName;
	private String nationality;
}

