package bg.duosoft.ipas.rest.client;

import bg.duosoft.ipas.rest.custommodel.ipobject.RIpObjectEfilingUserRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.efiling.RUserdocEfilingUserRequest;

/**
 * Created by Raya
 * 11.09.2020
 */
public interface EfilingDataRestClient {

    String updateUserdocEfilingLogUserName(RUserdocEfilingUserRequest request);

    String updateIpObjectEfilingLogUserName(RIpObjectEfilingUserRequest request);
}
