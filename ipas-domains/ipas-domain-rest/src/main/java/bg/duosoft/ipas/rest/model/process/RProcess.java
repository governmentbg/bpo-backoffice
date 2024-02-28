package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.miscellaneous.RStatus;
import bg.duosoft.ipas.rest.model.miscellaneous.RSubstatus;
import bg.duosoft.ipas.rest.model.person.RUser;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RProcess implements Serializable {
	private String statusGroupName;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date creationDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date dueDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date statusDate;
	private String description;
	private String processTypeName;
	private RProcessId processId;
	private RStatus status;
	private RSubstatus substatus;
	private RUser responsibleUser;
	private Boolean indSignaturePending;
	private Boolean indFreezingJustEnded;
	private String endFreezeFlag;
	private List<RProcessFreezing> processFreezingList;
	private List<RProcessFrozen> processFrozenList;
	private List<RProcessEvent> processEventList;
	private RProcessOriginData processOriginData;
	public List<RProcessFreezing> getProcessFreezingList() {
		if (processFreezingList == null) {
			processFreezingList = new ArrayList<>();
		}
		return processFreezingList;
	}
	public List<RProcessFrozen> getProcessFrozenList() {
		if (processFrozenList == null) {
			processFrozenList = new ArrayList<>();
		}
		return processFrozenList;
	}
	public List<RProcessEvent> getProcessEventList() {
		if (processEventList == null) {
			processEventList = new ArrayList<>();
		}
		return processEventList;
	}
}

