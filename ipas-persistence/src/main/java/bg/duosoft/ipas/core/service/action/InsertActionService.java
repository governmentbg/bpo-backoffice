package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.model.process.CProcessInsertActionRequest;

public interface InsertActionService {

    void insertAction(CProcessInsertActionRequest insertActionRequest) throws InsertActionException;

}