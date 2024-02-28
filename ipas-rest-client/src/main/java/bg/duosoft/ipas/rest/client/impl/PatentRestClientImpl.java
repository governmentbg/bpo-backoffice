package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.PatentRestClient;
import bg.duosoft.ipas.rest.client.proxy.PatentRestProxy;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptPatentRequest;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:13
 */
@Service
public class PatentRestClientImpl  extends RestClientBaseImpl implements PatentRestClient {
    @Autowired
    private PatentRestProxy patentRestProxy;

    @Override
    public RAcceptIpObjectResponse acceptPatent(RAcceptPatentRequest rq) {
        return callService(rq, patentRestProxy::acceptPatent);
    }

    @Override
    public RPatent getPatent(RFileId fileId, boolean addAttachments) {
        return callService(new RGetIpObjectRequest(fileId, addAttachments), patentRestProxy::getPatent);
    }

    @Override
    public void updatePatent(RPatent patent) {
        callService(patent, patentRestProxy::updatePatent);
    }

    @Override
    public void deletePatent(RFileId fileId) {
        callService(fileId, patentRestProxy::deletePatent);
    }

    @Override
    public boolean isMainEpoPatentRequestForValidation(RDocumentId documentId) {
        return callService(documentId, patentRestProxy::isMainEpoPatentRequestForValidation);
    }
}
