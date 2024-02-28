package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.person.RUser;
import bg.duosoft.ipas.rest.model.action.RJournal;
import bg.duosoft.ipas.rest.model.action.RActionId;
import bg.duosoft.ipas.rest.model.miscellaneous.RStatus;
import bg.duosoft.ipas.rest.model.action.RActionType;
import bg.duosoft.ipas.rest.model.offidoc.ROffidoc;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RActionProcessEvent implements Serializable {
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date actionDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date manualDueDate;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date captureDate;
	private String notes1;
	private String notes2;
	private String notes3;
	private String notes4;
	private String notes5;
	private String notes;
	private Boolean indCancelled;
	private Boolean indSignaturePending;
	private RUser responsibleUser;
	private RUser captureUser;
	private RJournal journal;
	private Boolean indChangesStatus;
	private RActionId actionId;
	private RStatus oldStatus;
	private RStatus newStatus;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date oldStatusDate;
	private RUser oldResponsibleUser;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date oldExpirationDate;
	private RActionType actionType;
	private boolean isLastActionInProcess;
	private Boolean indDeleted;
	private ROffidoc generatedOffidoc;
}

