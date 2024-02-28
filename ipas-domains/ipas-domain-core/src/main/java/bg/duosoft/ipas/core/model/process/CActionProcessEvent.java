package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.action.CJournal;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.person.CUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CActionProcessEvent implements Serializable {
    private static final long serialVersionUID = 2886500602209120639L;
    private Date actionDate;
    private Date manualDueDate;
    private Date captureDate;
    private String notes1;
    private String notes2;
    private String notes3;
    private String notes4;
    private String notes5;
    private String notes;
    private Boolean indCancelled;
    private Boolean indSignaturePending;
    private CUser responsibleUser;
    private CUser captureUser;
    private CJournal journal;
    private Boolean indChangesStatus;
    private CActionId actionId;
    private CStatus oldStatus;
    private CStatus newStatus;
    private Date oldStatusDate;
    private CUser oldResponsibleUser;
    private Date oldExpirationDate;
    private CActionType actionType;
    private boolean isLastActionInProcess;
    private Boolean indDeleted;
    private COffidoc generatedOffidoc;
}
