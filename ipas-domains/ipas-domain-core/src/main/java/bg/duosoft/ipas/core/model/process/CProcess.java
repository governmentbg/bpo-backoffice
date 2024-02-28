package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.miscellaneous.CSubstatus;
import bg.duosoft.ipas.core.model.person.CUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CProcess implements Serializable {
    private static final long serialVersionUID = -7968657337876145214L;
    private String statusGroupName;
    private Date creationDate;
    private Date dueDate;
    private Date statusDate;
    private String description;
    private String processTypeName;
    private CProcessId processId;
    private CStatus status;
    private CSubstatus substatus;
    private CUser responsibleUser;
    private Boolean indSignaturePending;
    private Boolean indFreezingJustEnded;
    private String endFreezeFlag;
    private List<CProcessFreezing> processFreezingList;
    private List<CProcessFrozen> processFrozenList;
    private List<CProcessEvent> processEventList;
    private CProcessOriginData processOriginData;
}