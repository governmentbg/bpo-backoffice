package bg.duosoft.ipas.rest.model.patent;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.design.RPatentLocarnoClasses;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RTechnicalData implements Serializable {
	private String title;
	private String englishTitle;
	private byte[] wordfileTitle;
	private String mainAbstract;
	private String englishAbstract;
	private String lastClaimsPageRef;
	private String lastDescriptionPageRef;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date noveltyDate;
	private Boolean hasIpc;
	private Boolean hasCpc;
	private List<RPatentLocarnoClasses> locarnoClassList;
	private List<RPatentIpcClass> ipcClassList;
	private List<RPatentCpcClass> cpcClassList;
	private List<RClaim> claimList;
	private List<RDrawing> drawingList;
	private List<RPatentCitation> citationList;
	public List<RPatentLocarnoClasses> getLocarnoClassList() {
		if (locarnoClassList == null) {
			locarnoClassList = new ArrayList<>();
		}
		return locarnoClassList;
	}
	public List<RPatentIpcClass> getIpcClassList() {
		if (ipcClassList == null) {
			ipcClassList = new ArrayList<>();
		}
		return ipcClassList;
	}
	public List<RClaim> getClaimList() {
		if (claimList == null) {
			claimList = new ArrayList<>();
		}
		return claimList;
	}
	public List<RDrawing> getDrawingList() {
		if (drawingList == null) {
			drawingList = new ArrayList<>();
		}
		return drawingList;
	}
	public List<RPatentCitation> getCitationList() {
		if (citationList == null) {
			citationList = new ArrayList<>();
		}
		return citationList;
	}
}

