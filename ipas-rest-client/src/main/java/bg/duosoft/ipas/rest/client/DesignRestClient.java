package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.design.RAcceptDesignRequest;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.patent.RPatent;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
public interface DesignRestClient {
    RAcceptIpObjectResponse acceptDesign(RAcceptDesignRequest rq);
    RPatent getSingleDesign(RFileId fileId, boolean addAttachments);
}
