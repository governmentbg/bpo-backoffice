package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.EfilingDataRestClient;
import bg.duosoft.ipas.rest.client.proxy.EfilingDataRestProxy;
import bg.duosoft.ipas.rest.custommodel.ipobject.RIpObjectEfilingUserRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.efiling.RUserdocEfilingUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Raya
 * 11.09.2020
 */
@Service
public class EfilingDataRestClientImpl extends RestClientBaseImpl implements EfilingDataRestClient {

    @Autowired
    private EfilingDataRestProxy efilingDataRestProxy;

    @Override
    public String updateUserdocEfilingLogUserName(RUserdocEfilingUserRequest request) {
        return callService(request, efilingDataRestProxy::updateUserdocEfilingLogUserName);
    }

    @Override
    public String updateIpObjectEfilingLogUserName(RIpObjectEfilingUserRequest request) {
        return callService(request, efilingDataRestProxy::updateIpObjectEfilingLogUserName);
    }
}
