package bg.duosoft.ipas.rest.client;

import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocAbdocsDataResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocUpdateNotificationReadDateRequest;

public interface OffidocRestClient {

    ROffidocAbdocsDataResponse selectOffidocByHashKey(String key);
    EmptyResponse updateEmailNotificationReadDate(ROffidocUpdateNotificationReadDateRequest request);
    EmptyResponse updatePortalNotificationReadDate(ROffidocUpdateNotificationReadDateRequest request);

}
