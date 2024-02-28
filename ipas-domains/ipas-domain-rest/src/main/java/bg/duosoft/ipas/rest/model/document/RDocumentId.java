package bg.duosoft.ipas.rest.model.document;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RDocumentId implements Serializable {
	private String docOrigin;
	private String docLog;
	private Integer docSeries;
	private Integer docNbr;
}

