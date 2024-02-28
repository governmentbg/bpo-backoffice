package bg.duosoft.ipas.rest.client.proxy;

import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.sql.RExecuteSqlRequest;
import bg.duosoft.ipas.rest.custommodel.sql.RSelectRowsRequest;
import bg.duosoft.ipas.rest.model.process.RProcess;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 17.7.2020 г.
 * Time: 15:01
 */
@Path("/sql")
public interface SqlRestProxy {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/selectToObjectArray")
    RestApiResponse<List<Object[]>> selectToObjectArray(RestApiRequest<RSelectRowsRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/selectToMap")
    RestApiResponse<List<Map<String, Object>>> selectToMap(RestApiRequest<RSelectRowsRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/execute")
    RestApiResponse<Integer> executeSql(RestApiRequest<RExecuteSqlRequest> rq);
}
