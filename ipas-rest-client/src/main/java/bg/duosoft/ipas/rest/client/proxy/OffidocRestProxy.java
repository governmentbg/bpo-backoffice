package bg.duosoft.ipas.rest.client.proxy;

import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocAbdocsDataResponse;
import bg.duosoft.ipas.rest.custommodel.offidoc.ROffidocUpdateNotificationReadDateRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/offidoc")
public interface OffidocRestProxy {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/selectOffidocByHashKey")
    RestApiResponse<ROffidocAbdocsDataResponse> selectOffidocByHashKey(RestApiRequest<String> key);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateEmailNotificationReadDate")
    RestApiResponse<EmptyResponse> updateEmailNotificationReadDate(RestApiRequest<ROffidocUpdateNotificationReadDateRequest> request);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updatePortalNotificationReadDate")
    RestApiResponse<EmptyResponse> updatePortalNotificationReadDate(RestApiRequest<ROffidocUpdateNotificationReadDateRequest> request);

}
