package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.process.CNextProcessAction;
import bg.duosoft.ipas.core.model.process.CProcess;
import bg.duosoft.ipas.core.model.process.CProcessInsertActionRequest;
import bg.duosoft.ipas.services.core.IpasServiceException;

public interface InsertActionAdditionalDataService {

    void insertAdditionalData(CProcess process, CActionId actionId, CProcessInsertActionRequest insertActionRequest, CNextProcessAction nextProcessAction) throws RuntimeException, IpasServiceException;

}