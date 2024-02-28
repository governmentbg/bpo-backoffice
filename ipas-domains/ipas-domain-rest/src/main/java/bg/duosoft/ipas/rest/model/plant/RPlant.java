package bg.duosoft.ipas.rest.model.plant;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPlant implements Serializable {
	private String taxonCode;
	private String proposedDenomination;
	private String proposedDenominationEng;
	private String publDenomination;
	private String publDenominationEng;
	private String apprDenomination;
	private String apprDenominationEng;
	private String rejDenomination;
	private String rejDenominationEng;
	private String features;
	private String stability;
	private String testing;
	private RPlantTaxonNomenclature plantNumenclature;
}

