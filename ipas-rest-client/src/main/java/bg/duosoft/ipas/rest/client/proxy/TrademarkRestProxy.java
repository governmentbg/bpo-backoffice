package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.mark.international_registration.RAcceptIntlTradeMarkRequest;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.mark.RAcceptTradeMarkRequest;
import bg.duosoft.ipas.rest.model.mark.RMark;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
@Path("/mark")
public interface TrademarkRestProxy {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get")
    RestApiResponse<RMark> getMark(RestApiRequest<RGetIpObjectRequest> request);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept")
    RestApiResponse<RAcceptIpObjectResponse> acceptTrademark(RestApiRequest<RAcceptTradeMarkRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/acceptInternationalMark")
    RestApiResponse<RAcceptIpObjectResponse> acceptInternationalTrademark(RestApiRequest<RAcceptIntlTradeMarkRequest> rq);
}
