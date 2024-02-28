package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.TrademarkRestClient;
import bg.duosoft.ipas.rest.client.proxy.TrademarkRestProxy;
import bg.duosoft.ipas.rest.custommodel.ipobject.RAcceptIpObjectResponse;
import bg.duosoft.ipas.rest.custommodel.ipobject.RGetIpObjectRequest;
import bg.duosoft.ipas.rest.custommodel.mark.RAcceptTradeMarkRequest;
import bg.duosoft.ipas.rest.custommodel.mark.international_registration.RAcceptIntlTradeMarkRequest;
import bg.duosoft.ipas.rest.model.mark.RMark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:15
 */
@Service
public class TrademarkRestClientImpl extends RestClientBaseImpl implements TrademarkRestClient {
    @Autowired
    private TrademarkRestProxy trademarkRestProxy;

    @Override
    public RMark getMark(RGetIpObjectRequest request) {
        return callService(request, trademarkRestProxy::getMark);
    }

    @Override
    public RAcceptIpObjectResponse acceptTrademark(RAcceptTradeMarkRequest rq) {
        return callService(rq, trademarkRestProxy::acceptTrademark);
    }

    @Override
    public RAcceptIpObjectResponse acceptInternationalTrademark(RAcceptIntlTradeMarkRequest rq) {
        return callService(rq, trademarkRestProxy::acceptInternationalTrademark);
    }
}
