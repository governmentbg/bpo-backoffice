package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.ProcessRestClient;
import bg.duosoft.ipas.rest.client.proxy.ProcessRestProxy;
import bg.duosoft.ipas.rest.custommodel.action.RDeleteActionRequest;
import bg.duosoft.ipas.rest.custommodel.process.RGetProcessRequest;
import bg.duosoft.ipas.rest.model.file.RProcessId;
import bg.duosoft.ipas.rest.model.process.RProcess;
import bg.duosoft.ipas.rest.model.process.RProcessInsertActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:14
 */
@Service
public class ProcessRestClientImpl extends RestClientBaseImpl implements ProcessRestClient {
    @Autowired
    private ProcessRestProxy processRestProxy;

    @Override
    public RProcess getProcess(RProcessId processId, boolean addProcessEvents) {
        return callService(new RGetProcessRequest(processId, addProcessEvents), processRestProxy::getProcess);
    }

    @Override
    public void insertAction(RProcessInsertActionRequest rq) {
        callService(rq, processRestProxy::insertAction);
    }
    public boolean deleteAction(RDeleteActionRequest rq) {
        return callService(rq, processRestProxy::deleteAction);
    }
}
