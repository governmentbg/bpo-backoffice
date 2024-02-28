package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.mark.RAcceptTradeMarkRequest;
import bg.duosoft.ipas.rest.custommodel.mark.international_registration.RAcceptIntlTradeMarkRequest;
import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.mark.RMark;

import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
public interface TrademarkRestClient {
    RMark getMark(RGetIpObjectRequest request);

    RAcceptIpObjectResponse acceptTrademark(RAcceptTradeMarkRequest rq);

    RAcceptIpObjectResponse acceptInternationalTrademark(RAcceptIntlTradeMarkRequest rq);
}
