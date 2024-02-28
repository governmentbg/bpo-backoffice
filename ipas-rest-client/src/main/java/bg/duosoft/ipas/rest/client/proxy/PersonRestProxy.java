package bg.duosoft.ipas.rest.client.proxy;

import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.person.RGetPersonRequest;
import bg.duosoft.ipas.rest.model.person.RPerson;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: Georgi
 * Date: 21.7.2020 г.
 * Time: 15:38
 */
@Path("/person")
public interface PersonRestProxy {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get")
    public RestApiResponse<RPerson> getPerson(RestApiRequest<RGetPersonRequest> rq);
}
