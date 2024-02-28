package bg.duosoft.ipas.rest.model.mark;

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
public class RProtectionData implements Serializable {
	private List<RNiceClass> niceClassList;
	public List<RNiceClass> getNiceClassList() {
		if (niceClassList == null) {
			niceClassList = new ArrayList<>();
		}
		return niceClassList;
	}
}

