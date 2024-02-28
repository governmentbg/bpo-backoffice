package bg.duosoft.ipas.rest.model.userdoc.grounds;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocSubGrounds implements Serializable {
	private Integer rootGroundId;
	private Integer legalGroundTypeId;
	private String legalGroundTypeTitle;
	private String legalGroundTypeDescription;
	private String version;
}

