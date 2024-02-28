package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.CommonServicesRestClient;
import bg.duosoft.ipas.rest.client.proxy.CommonServicesRestProxy;
import bg.duosoft.ipas.rest.custommodel.EmptyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:10
 */
@Service
public class CommonServicesRestClientImpl  extends RestClientBaseImpl implements CommonServicesRestClient {
    @Autowired
    private CommonServicesRestProxy commonServicesRestProxy;

    @Override
    public Date getWorkingDate() {
        return callService(new EmptyRequest(), commonServicesRestProxy::getWorkingDate);
    }
}
