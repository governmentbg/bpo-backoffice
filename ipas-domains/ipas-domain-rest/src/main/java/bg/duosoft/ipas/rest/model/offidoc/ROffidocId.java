package bg.duosoft.ipas.rest.model.offidoc;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ROffidocId implements Serializable {
	private String offidocOrigin;
	private Integer offidocSeries;
	private Integer offidocNbr;
}

