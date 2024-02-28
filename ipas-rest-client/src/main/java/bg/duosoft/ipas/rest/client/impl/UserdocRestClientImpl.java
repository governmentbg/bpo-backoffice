package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.UserdocRestClient;
import bg.duosoft.ipas.rest.client.proxy.UserdocRestProxy;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:24
 */
@Service
public class UserdocRestClientImpl extends RestClientBaseImpl implements UserdocRestClient {
    @Autowired
    private UserdocRestProxy userdocRestProxy;
    @Override
    public RAcceptedUserdocResponse acceptUserdoc(RAcceptUserdocRequest rq) {
        return callService(rq, userdocRestProxy::acceptUserdoc);
    }

    @Override
    public RUserdoc getUserdoc(RDocumentId documentId) {
        return callService(documentId, userdocRestProxy::getUserdoc);
    }

    @Override
    public void deleteUserdoc(RDocumentId documentId,  boolean deleteInDocflowSystem) {
        callService(new RDeleteUserdocRequest(documentId, deleteInDocflowSystem) , userdocRestProxy::deleteUserdoc);
    }

    @Override
    public List<RUserdocHierarchyNode> getFileUserdocHierarchy(RFileId fileId, boolean flatHierarchy) {
        return callService(new RGetFileUserdocHierarchyRequest(fileId, flatHierarchy), userdocRestProxy::getFileUserdocsHierarchy);
    }

    @Override
    public RGetFileUserdocHierarchyFilteredResponse getFileUserdocHierarchyFiltered(RFileId fileId, boolean flatHierarchy, RUserdocHierarchyFilter filter) {
        return callService(new RGetFileUserdocHierarchyFilteredRequest(fileId, flatHierarchy, filter), userdocRestProxy::getFileUserdocsHierarchyFiltered);
    }

    @Override
    public List<RUserdocHierarchyNode> getUserdocUserdocHierarchy(RDocumentId documentId, boolean flatHierarchy) {
        return callService(new RGetUserdocUserdocHierarchyRequest(documentId, flatHierarchy), userdocRestProxy::getUserdocUserdocsHierarchy);
    }

    @Override
    public RAcceptedInternationalRegistrationResponse acceptInternationalRegistrationUserdoc(RAcceptInternationalRegistrationRequest rq) {
        return callService(rq, userdocRestProxy::acceptInternationalRegistrationUserdoc);
    }

    @Override
    public RAcceptedUserdocResponse acceptMadridEfilingUserdoc(RAcceptMadridEfilingUserdocRequest rq) {
        return callService(rq, userdocRestProxy::acceptMadridEfilingUserdoc);
    }

    @Override
    public RAcceptedUserdocResponse acceptInternationalMarkUserdoc(RAcceptInternationalUserdocRequest rq) {
        return callService(rq, userdocRestProxy::acceptInternationalMarkUserdoc);
    }

    @Override
    public RAcceptedUserdocResponse acceptWipoIrregularityLetters(RAcceptIrregLetterUserdocRequest rq) {
        return callService(rq, userdocRestProxy::acceptWipoIrregularityLetters);
    }

    @Override
    public RFileId getUserdocMasterFileId(RDocumentId documentId) {
        return callService(documentId, userdocRestProxy::getUserdocMasterFileId);
    }
}
