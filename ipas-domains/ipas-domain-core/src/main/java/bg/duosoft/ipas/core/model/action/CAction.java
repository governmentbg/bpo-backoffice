package bg.duosoft.ipas.core.model.action;

import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.person.CUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CAction implements Serializable {
    private static final long serialVersionUID = 2886500602209120639L;
    private CActionId actionId;
    private Date actionDate;
    private String notes1;
    private String notes2;
    private String notes3;
    private String notes4;
    private String notes5;
    private String notes;
    private Boolean indCancelled;
    private Boolean indSignaturePending;
    private Boolean indDeleted;
    private Integer deleteUserId;
    private Date deleteDate;
    private String deleteReason;
    private Boolean indChk1;
    private Boolean indChk2;
    private Boolean indChk3;
    private Boolean indChk4;
    private Boolean indChk5;
    private CUser responsibleUser;
    private CUser captureUser;
    private CJournal journal;
    private CStatus oldStatus;
    private CStatus newStatus;
    private CActionType actionType;
    private COffidoc offidoc;

}
