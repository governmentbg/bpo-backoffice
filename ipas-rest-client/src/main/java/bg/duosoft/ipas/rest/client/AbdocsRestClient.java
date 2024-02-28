package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.EmptyResponse;
import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;
import bg.duosoft.ipas.rest.custommodel.abdocs.document.*;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
public interface AbdocsRestClient {

    RAbdocsDocumentContentResponse getDocumentContent(RAbdocsDocumentContentRequest rq);
    RAbdocsDocFilesResponse getDocFiles(RAbdocsDocFilesRequest rq);
    EmptyResponse updateFileContent(RAbdocUpdateFileContentRequest rq);
    EmptyResponse insertFile(RAbdocUpdateFileContentRequest rq);
    RAbdocsDownloadFileResponse downloadFile(RAbdocsDownloadFileRequest rq);
    Boolean checkIfOffidocEmailExist(RAbdocsOffidocEmailRequest rq);
    EmptyResponse updateOffidocEmailStatusAsRead(RAbdocsOffidocEmailRequest rq);
    RAbdocsSimpleDocumentResponse selectSimpleDocumentByHash(String rq);

}
