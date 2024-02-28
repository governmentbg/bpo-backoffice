package bg.duosoft.ipas.rest.model.userdoc.court_appeal;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.miscellaneous.RCourt;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RUserdocCourtAppeal implements Serializable {
	private Integer courtAppealId;
	private String courtCaseNbr;
	private String courtLink;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date courtCaseDate;
	private String judicialActNbr;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date judicialActDate;
	private RCourt court;
	private RJudicialActType judicialActType;
}

