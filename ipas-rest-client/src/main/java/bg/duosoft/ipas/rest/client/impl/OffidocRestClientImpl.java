package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.OffidocRestClient;
import bg.duosoft.ipas.rest.client.proxy.OffidocRestProxy;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocAbdocsDataResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocUpdateNotificationReadDateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OffidocRestClientImpl extends RestClientBaseImpl implements OffidocRestClient {

    @Autowired
    private OffidocRestProxy offidocRestProxy;

    @Override
    public ROffidocAbdocsDataResponse selectOffidocByHashKey(String key) {
        return callService(key, offidocRestProxy::selectOffidocByHashKey);
    }

    @Override
    public EmptyResponse updateEmailNotificationReadDate(ROffidocUpdateNotificationReadDateRequest request) {
        return callService(request, offidocRestProxy::updateEmailNotificationReadDate);
    }

    @Override
    public EmptyResponse updatePortalNotificationReadDate(ROffidocUpdateNotificationReadDateRequest request) {
        return callService(request, offidocRestProxy::updatePortalNotificationReadDate);
    }
}
