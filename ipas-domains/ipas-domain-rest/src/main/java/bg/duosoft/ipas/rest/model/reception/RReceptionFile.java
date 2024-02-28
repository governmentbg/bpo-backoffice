package bg.duosoft.ipas.rest.model.reception;

import com.fasterxml.jackson.annotation.*;
import bg.duosoft.ipas.rest.model.file.RFileId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RReceptionFile implements Serializable {
	private RFileId fileId;
	private String title;
	private boolean emptyTitle;
	private String applicationType;
	private String applicationSubType;
}

