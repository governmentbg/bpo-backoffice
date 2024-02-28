package bg.duosoft.ipas.rest.model.plant;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPlantTaxonNomenclature implements Serializable {
	private Long id;
	private String taxonCode;
	private String commonClassifyBul;
	private String commonClassifyEng;
	private String latinClassify;
}

