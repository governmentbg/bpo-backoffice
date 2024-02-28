package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CProcessEvent implements Serializable {
    private static final long serialVersionUID = -4932955349263876320L;
    private Date eventDate;
    private String eventDescription;
    private String eventNotes;
    private String eventType;
    private String eventTypeCode;
    private String eventUser;
    private String eventStatus;
    private CProcessId eventProcessId;
    private Boolean indEventProcessPending;
    private Date actionJournalDate;
    private String actionJournalCode;
    private CActionProcessEvent eventAction;
    private CUserdocProcessEvent eventUserdoc;
}
