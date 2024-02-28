package bg.duosoft.ipas.rest.model.design;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RSingleDesignExtended implements Serializable {
	private Long drawingNbr;
	private Boolean imageRefused;
	private Boolean imagePublished;
	private RImageViewType imageViewType;
}

