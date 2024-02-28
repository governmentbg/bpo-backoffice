package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.ReceptionRestClient;
import bg.duosoft.ipas.rest.client.proxy.ReceptionRestProxy;
import bg.duosoft.ipas.rest.model.reception.RReception;
import bg.duosoft.ipas.rest.model.reception.RReceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Georgi
 * Date: 11.9.2020 г.
 * Time: 9:46
 */
@Service
public class ReceptionRestClientImpl extends RestClientBaseImpl implements ReceptionRestClient {
    @Autowired
    private ReceptionRestProxy receptionRestProxy;

    @Override
    public RReceptionResponse createReception(RReception request) {
        return callService(request, receptionRestProxy::createReception);
    }
}
