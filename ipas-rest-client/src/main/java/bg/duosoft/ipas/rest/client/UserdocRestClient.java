package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.userdoc.RAcceptUserdocRequest;
import bg.duosoft.ipas.rest.custommodel.userdoc.RAcceptedUserdocResponse;
import bg.duosoft.ipas.rest.custommodel.userdoc.RUserdocHierarchyFilter;
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

import java.util.List;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
public interface UserdocRestClient {
    RAcceptedUserdocResponse acceptUserdoc(RAcceptUserdocRequest rq);
    RUserdoc getUserdoc(RDocumentId documentId);
    void deleteUserdoc(RDocumentId documentId, boolean deleteInDocflowSystem);
    public List<RUserdocHierarchyNode> getFileUserdocHierarchy(RFileId fileId, boolean flatHierarchy);
    public RGetFileUserdocHierarchyFilteredResponse getFileUserdocHierarchyFiltered(RFileId fileId, boolean flatHierarchy, RUserdocHierarchyFilter filter);
    public List<RUserdocHierarchyNode> getUserdocUserdocHierarchy(RDocumentId fileId, boolean flatHierarchy);
    public RFileId getUserdocMasterFileId(RDocumentId documentId);
    RAcceptedInternationalRegistrationResponse acceptInternationalRegistrationUserdoc(RAcceptInternationalRegistrationRequest rq);
    RAcceptedUserdocResponse acceptMadridEfilingUserdoc(RAcceptMadridEfilingUserdocRequest rq);
    RAcceptedUserdocResponse acceptWipoIrregularityLetters(RAcceptIrregLetterUserdocRequest rq);
    RAcceptedUserdocResponse acceptInternationalMarkUserdoc(RAcceptInternationalUserdocRequest rq);
}
