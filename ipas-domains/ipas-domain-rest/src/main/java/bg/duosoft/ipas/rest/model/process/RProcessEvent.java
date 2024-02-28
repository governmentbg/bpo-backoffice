package bg.duosoft.ipas.rest.model.process;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RProcessEvent implements Serializable {
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date eventDate;
	private String eventDescription;
	private String eventNotes;
	private String eventType;
	private String eventTypeCode;
	private String eventUser;
	private String eventStatus;
	private RProcessId eventProcessId;
	private Boolean indEventProcessPending;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date actionJournalDate;
	private String actionJournalCode;
	private RActionProcessEvent eventAction;
	private RUserdocProcessEvent eventUserdoc;
}

