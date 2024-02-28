package bg.duosoft.ipas.rest.client.proxy;

import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.model.reception.RReception;
import bg.duosoft.ipas.rest.model.reception.RReceptionResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: Georgi
 * Date: 11.9.2020 г.
 * Time: 9:45
 */
@Path("/reception")
public interface ReceptionRestProxy {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    RestApiResponse<RReceptionResponse> createReception(RestApiRequest<RReception> rq);

}
