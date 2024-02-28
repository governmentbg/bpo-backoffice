package bg.duosoft.ipas.rest.client.proxy;


import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.userdoc.*;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_mark.RAcceptInternationalUserdocRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_registration.RAcceptInternationalRegistrationRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_registration.RAcceptMadridEfilingUserdocRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.international_registration.RAcceptedInternationalRegistrationResponse;
import bg.duosoft.ipas.rest.custommodel.userdoc.irregularity_letter.RAcceptIrregLetterUserdocRequest;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.userdoc.RGetFileUserdocHierarchyFilteredResponse;
import bg.duosoft.ipas.rest.model.userdoc.RUserdoc;
import bg.duosoft.ipas.rest.model.userdoc.RUserdocHierarchyNode;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
@Path("/userdoc")
public interface UserdocRestProxy {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept")
    RestApiResponse<RAcceptedUserdocResponse> acceptUserdoc(RestApiRequest<RAcceptUserdocRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get")
    RestApiResponse<RUserdoc> getUserdoc(RestApiRequest<RDocumentId> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete")
    RestApiResponse<EmptyResponse> deleteUserdoc(RestApiRequest<RDeleteUserdocRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get-file-hierarchy")
    RestApiResponse<List<RUserdocHierarchyNode>> getFileUserdocsHierarchy(RestApiRequest<RGetFileUserdocHierarchyRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get-file-hierarchy-filtered")
    RestApiResponse<RGetFileUserdocHierarchyFilteredResponse> getFileUserdocsHierarchyFiltered(RestApiRequest<RGetFileUserdocHierarchyFilteredRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get-userdoc-hierarchy")
    RestApiResponse<List<RUserdocHierarchyNode>> getUserdocUserdocsHierarchy(RestApiRequest<RGetUserdocUserdocHierarchyRequest> rq);


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/get-userdoc-master-file-id")
    RestApiResponse<RFileId> getUserdocMasterFileId(RestApiRequest<RDocumentId> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept-international-registration")
    RestApiResponse<RAcceptedInternationalRegistrationResponse> acceptInternationalRegistrationUserdoc(RestApiRequest<RAcceptInternationalRegistrationRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept-madrid-efiling-userdoc")
    RestApiResponse<RAcceptedUserdocResponse> acceptMadridEfilingUserdoc(RestApiRequest<RAcceptMadridEfilingUserdocRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept-international-mark-userdoc")
    RestApiResponse<RAcceptedUserdocResponse> acceptInternationalMarkUserdoc(RestApiRequest<RAcceptInternationalUserdocRequest> rq);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/accept-wipo-irreg-letter-userdoc")
    RestApiResponse<RAcceptedUserdocResponse> acceptWipoIrregularityLetters(RestApiRequest<RAcceptIrregLetterUserdocRequest> rq);
}
