package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.action.RDeleteActionRequest;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptPatentRequest;
import bg.duosoft.ipas.rest.custommodel.process.RGetProcessRequest;
import bg.duosoft.ipas.rest.model.process.RProcess;
import bg.duosoft.ipas.rest.model.process.RProcessInsertActionRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: Georgi
 * Date: 16.7.2020 г.
 * Time: 23:52
 */
@Path("/process")
public interface ProcessRestProxy {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get")
    RestApiResponse<RProcess> getProcess(RestApiRequest<RGetProcessRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/action/insert")
    RestApiResponse<EmptyResponse> insertAction(RestApiRequest<RProcessInsertActionRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/action/delete")
    RestApiResponse<Boolean> deleteAction(RestApiRequest<RDeleteActionRequest> rq);
}
