package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.action.RDeleteActionRequest;
import bg.duosoft.ipas.rest.custommodel.process.RGetProcessRequest;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.process.RProcess;
import bg.duosoft.ipas.rest.model.process.RProcessInsertActionRequest;

/**
 * User: Georgi
 * Date: 16.7.2020 г.
 * Time: 23:52
 */
public interface ProcessRestClient {
    RProcess getProcess(RProcessId processId, boolean addProcessEvents);
    void insertAction(RProcessInsertActionRequest rq);
    boolean deleteAction(RDeleteActionRequest rq);
}
