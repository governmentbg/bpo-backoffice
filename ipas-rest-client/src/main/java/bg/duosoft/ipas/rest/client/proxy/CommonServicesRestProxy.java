package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.EmptyRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
public interface CommonServicesRestProxy {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getWorkingDate")
    RestApiResponse<Date> getWorkingDate(RestApiRequest<EmptyRequest> rq);
}
