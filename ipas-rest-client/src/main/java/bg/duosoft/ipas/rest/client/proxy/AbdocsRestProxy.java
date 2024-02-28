package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.abdocs.document.*;

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
@Path("/abdocs")
public interface AbdocsRestProxy {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getDocumentContent")
    RestApiResponse<RAbdocsDocumentContentResponse> getDocumentContent(RestApiRequest<RAbdocsDocumentContentRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getDocFiles")
    RestApiResponse<RAbdocsDocFilesResponse> getDocFiles(RestApiRequest<RAbdocsDocFilesRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertFile")
    RestApiResponse<EmptyResponse> insertFile(RestApiRequest<RAbdocUpdateFileContentRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateFileContent")
    RestApiResponse<EmptyResponse> updateFileContent(RestApiRequest<RAbdocUpdateFileContentRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/downloadFile")
    RestApiResponse<RAbdocsDownloadFileResponse> downloadFile(RestApiRequest<RAbdocsDownloadFileRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/checkIfOffidocEmailExist")
    RestApiResponse<Boolean> checkIfOffidocEmailExist(RestApiRequest<RAbdocsOffidocEmailRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateOffidocEmailStatusAsRead")
    RestApiResponse<EmptyResponse> updateOffidocEmailStatusAsRead(RestApiRequest<RAbdocsOffidocEmailRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/selectSimpleDocumentByHash")
    RestApiResponse<RAbdocsSimpleDocumentResponse> selectSimpleDocumentByHash(RestApiRequest<String> rq);

}
