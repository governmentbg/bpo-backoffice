package bg.duosoft.ipas.rest.model.document;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RDocumentSeqId implements Serializable {
	private String docSeqType;
	private String docSeqName;
	private Integer docSeqNbr;
	private Integer docSeqSeries;
}

