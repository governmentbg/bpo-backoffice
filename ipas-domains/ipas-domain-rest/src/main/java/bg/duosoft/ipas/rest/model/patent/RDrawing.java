package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.design.RSingleDesignExtended;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RDrawing implements Serializable {
	private Long drawingNbr;
	private String drawingType;
	private byte[] drawingData;
	private RSingleDesignExtended singleDesignExtended;
}

