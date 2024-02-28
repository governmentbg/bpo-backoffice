package bg.duosoft.ipas.rest.model.userdoc.grounds;

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
public class RUserdocRootGrounds implements Serializable {
	private Integer rootGroundId;
	private String motives;
	private String groundCommonText;
	private REarlierRightTypes earlierRightType;
	private RApplicantAuthority applicantAuthority;
	private List<RUserdocSubGrounds> userdocSubGrounds;
	private RMarkGroundData markGroundData;
	private RPatentGroundData patentGroundData;
	private List<RSingleDesignGroundData> singleDesignGroundData;
	public List<RUserdocSubGrounds> getUserdocSubGrounds() {
		if (userdocSubGrounds == null) {
			userdocSubGrounds = new ArrayList<>();
		}
		return userdocSubGrounds;
	}
	public List<RSingleDesignGroundData> getSingleDesignGroundData() {
		if (singleDesignGroundData == null) {
			singleDesignGroundData = new ArrayList<>();
		}
		return singleDesignGroundData;
	}
}

