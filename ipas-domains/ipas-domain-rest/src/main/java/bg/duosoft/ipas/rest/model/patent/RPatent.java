package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.file.RFile;
import bg.duosoft.ipas.rest.model.plant.RPlant;
import bg.duosoft.ipas.rest.model.spc.RSpcExtended;
import bg.duosoft.ipas.rest.model.file.RRelationshipExtended;
import bg.duosoft.ipas.rest.model.design.RProductTerm;
import bg.duosoft.ipas.rest.model.efiling.REFilingData;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPatent implements Serializable {
	private Boolean patentContainsDrawingList;
	private Boolean indReadDrawingList;
	private Integer rowVersion;
	private RPctApplicationData pctApplicationData;
	private RTechnicalData technicalData;
	private boolean reception;
	private RFile file;
	private RAuthorshipData authorshipData;
	private RPlant plantData;
	private RSpcExtended spcExtended;
	private RRelationshipExtended relationshipExtended;
	private RPatentDetails patentDetails;
	private RProductTerm productTerm;
	private REFilingData patentEFilingData;
}

