package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.design.RAcceptDesignRequest;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.model.patent.RPatent;

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
@Path("/design")
public interface DesignRestProxy {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept")
    RestApiResponse<RAcceptIpObjectResponse> acceptDesign(RestApiRequest<RAcceptDesignRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getSingleDesign")
    RestApiResponse<RPatent> getSingleDesign(RestApiRequest<RGetIpObjectRequest> rq);
}
