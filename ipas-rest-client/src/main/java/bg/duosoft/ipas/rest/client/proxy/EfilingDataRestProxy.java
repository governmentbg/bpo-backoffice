package bg.duosoft.ipas.rest.client.proxy;

import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RIpObjectEfilingUserRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.efiling.RUserdocEfilingUserRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Raya
 * 11.09.2020
 */
@Path("")
public interface EfilingDataRestProxy {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateUserdocEfilingLogUserName")
    RestApiResponse<String> updateUserdocEfilingLogUserName(RestApiRequest<RUserdocEfilingUserRequest> request);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateIpObjectEfilingLogUserName")
    RestApiResponse<String> updateIpObjectEfilingLogUserName(RestApiRequest<RIpObjectEfilingUserRequest> request);

}
