package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptPatentRequest;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
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
@Path("/patent")
public interface PatentRestProxy {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept")
    RestApiResponse<RAcceptIpObjectResponse> acceptPatent(RestApiRequest<RAcceptPatentRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get")
    RestApiResponse<RPatent> getPatent(RestApiRequest<RGetIpObjectRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update")
    RestApiResponse<Boolean> updatePatent(RestApiRequest<RPatent> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete")
    RestApiResponse<EmptyResponse> deletePatent(RestApiRequest<RFileId> rq);


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/isMainEpoPatentRequestForValidation")
    RestApiResponse<Boolean> isMainEpoPatentRequestForValidation(RestApiRequest<RDocumentId> rq);
}
