package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.AbdocsRestClient;
import bg.duosoft.ipas.rest.client.proxy.AbdocsRestProxy;
import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.abdocs.document.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbdocsRestClientImpl extends RestClientBaseImpl implements AbdocsRestClient {
    @Autowired
    private AbdocsRestProxy abdocsRestProxy;

    @Override
    public RAbdocsDocumentContentResponse getDocumentContent(RAbdocsDocumentContentRequest rq) {
        return callService(rq, abdocsRestProxy::getDocumentContent);
    }

    @Override
    public RAbdocsDocFilesResponse getDocFiles(RAbdocsDocFilesRequest rq) {
        return callService(rq, abdocsRestProxy::getDocFiles);
    }

    @Override
    public EmptyResponse updateFileContent(RAbdocUpdateFileContentRequest rq) {
        return callService(rq, abdocsRestProxy::updateFileContent);
    }

    @Override
    public EmptyResponse insertFile(RAbdocUpdateFileContentRequest rq) {
        return callService(rq, abdocsRestProxy::insertFile);
    }

    @Override
    public RAbdocsDownloadFileResponse downloadFile(RAbdocsDownloadFileRequest rq) {
        return callService(rq, abdocsRestProxy::downloadFile);
    }

    @Override
    public Boolean checkIfOffidocEmailExist(RAbdocsOffidocEmailRequest rq) {
        return callService(rq, abdocsRestProxy::checkIfOffidocEmailExist);
    }

    @Override
    public EmptyResponse updateOffidocEmailStatusAsRead(RAbdocsOffidocEmailRequest rq) {
        return callService(rq, abdocsRestProxy::updateOffidocEmailStatusAsRead);
    }

    @Override
    public RAbdocsSimpleDocumentResponse selectSimpleDocumentByHash(String rq) {
        return callService(rq, abdocsRestProxy::selectSimpleDocumentByHash);
    }

}
