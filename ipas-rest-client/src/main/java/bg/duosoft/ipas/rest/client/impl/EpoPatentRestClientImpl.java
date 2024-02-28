package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.EpoPatentRestClient;
import bg.duosoft.ipas.rest.client.proxy.EpoPatentRestProxy;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptEuPatentRequest;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptedEuPatentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:12
 */
@Service
public class EpoPatentRestClientImpl  extends RestClientBaseImpl implements EpoPatentRestClient {
    @Autowired
    private EpoPatentRestProxy epoPatentRestProxy;

    @Override
    public RAcceptedEuPatentResponse acceptEpoPatent(RAcceptEuPatentRequest rq) {
        return callService(rq, epoPatentRestProxy::acceptEpoPatent);
    }
}
