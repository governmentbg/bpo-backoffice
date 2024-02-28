package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.search.RSearchResponse;
import bg.duosoft.ipas.rest.model.search.RSearchParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * User: Georgi
 * Date: 17.9.2020 г.
 * Time: 22:58
 */
@Path("/search")
public interface SearchRestProxy {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/ipobjects")
    RestApiResponse<RSearchResponse> getDocumentContent(RestApiRequest<RSearchParam> rq);

}
