package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.process.CProcessAuthorizationData;

import java.util.Date;

public interface RecordalAuthorizationService {

    void authorize(CDocumentId documentId, CActionId actionId, CProcessAuthorizationData data);

}
