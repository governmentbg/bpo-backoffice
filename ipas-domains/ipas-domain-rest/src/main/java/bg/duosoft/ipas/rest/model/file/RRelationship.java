package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRelationship implements Serializable {
	private String relationshipType;
	private String relationshipRole;
	private RFileId fileId;
}

