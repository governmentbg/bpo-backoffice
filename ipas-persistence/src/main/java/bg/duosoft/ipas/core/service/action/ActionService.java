package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.model.action.CAction;
import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.file.CProcessId;

import java.util.Date;
import java.util.List;

public interface ActionService {

    CAction selectLastInsertedAction(String procTyp, Integer procNumber);

    CAction selectAction(CActionId cActionId);

    boolean isUserdocAuthorizationActionsExists(CProcessId processId);

    void updateAction(CAction cAction);

    boolean deleteAction(CActionId cActionId, Integer userId, String reason);

    boolean isActionExistsForProcessId(CProcessId processId);

    long count();

    Date selectMaxActionDateByDate(CProcessId processId, Date date);

    boolean hasPublications(String fileSeq, String fileType, Integer fileSer, Integer fileNbr);

}