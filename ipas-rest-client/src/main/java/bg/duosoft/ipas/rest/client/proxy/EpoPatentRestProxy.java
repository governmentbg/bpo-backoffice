package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptEuPatentRequest;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptedEuPatentResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
@Path("/epo-patent")
public interface EpoPatentRestProxy {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept")
    RestApiResponse<RAcceptedEuPatentResponse> acceptEpoPatent(RestApiRequest<RAcceptEuPatentRequest> rq);
}
