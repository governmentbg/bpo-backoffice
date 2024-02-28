package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.DesignRestClient;
import bg.duosoft.ipas.rest.client.proxy.DesignRestProxy;
import bg.duosoft.ipas.rest.custommodel.design.RAcceptDesignRequest;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.patent.RPatent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:11
 */
@Service
public class DesignRestClientImpl extends RestClientBaseImpl implements DesignRestClient {
    @Autowired
    private DesignRestProxy designRestProxy;

    @Override
    public RAcceptIpObjectResponse acceptDesign(RAcceptDesignRequest rq) {
        return callService(rq, designRestProxy::acceptDesign);
    }

    @Override
    public RPatent getSingleDesign(RFileId fileId, boolean addAttachments) {
        return callService(new RGetIpObjectRequest(fileId, addAttachments), designRestProxy::getSingleDesign);
    }
}
