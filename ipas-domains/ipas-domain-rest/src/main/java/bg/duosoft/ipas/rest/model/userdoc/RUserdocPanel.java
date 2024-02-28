package bg.duosoft.ipas.rest.model.userdoc;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocPanel implements Serializable {
	private String panel;
	private Boolean indRecordal;
	private String name;
	private String nameEn;
	private List<RUserdocExtraDataType> extraDataTypes;
	public List<RUserdocExtraDataType> getExtraDataTypes() {
		if (extraDataTypes == null) {
			extraDataTypes = new ArrayList<>();
		}
		return extraDataTypes;
	}
}

