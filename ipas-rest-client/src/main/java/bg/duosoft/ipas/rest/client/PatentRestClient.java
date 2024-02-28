package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.patent.RAcceptPatentRequest;
import bg.duosoft.ipas.rest.model.document.RDocumentId;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.patent.RPatent;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */

public interface PatentRestClient {
    RAcceptIpObjectResponse acceptPatent(RAcceptPatentRequest rq);
    RPatent getPatent(RFileId fileId, boolean addAttachments);
    void updatePatent(RPatent patent);
    void deletePatent(RFileId fileId);
    boolean isMainEpoPatentRequestForValidation(RDocumentId documentId);
}
