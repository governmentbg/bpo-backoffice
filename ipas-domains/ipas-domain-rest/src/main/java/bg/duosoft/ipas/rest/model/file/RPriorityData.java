package bg.duosoft.ipas.rest.model.file;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPriorityData implements Serializable {
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date earliestAcceptedParisPriorityDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date exhibitionDate;
	private String exhibitionNotes;
	private boolean hasExhibitionData;
	private boolean hasParisPriorityData;
	private List<RParisPriority> parisPriorityList;
	public List<RParisPriority> getParisPriorityList() {
		if (parisPriorityList == null) {
			parisPriorityList = new ArrayList<>();
		}
		return parisPriorityList;
	}
}

