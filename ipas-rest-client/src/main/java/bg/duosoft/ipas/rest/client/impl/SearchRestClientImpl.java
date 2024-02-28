package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.SearchRestClient;
import bg.duosoft.ipas.rest.client.proxy.SearchRestProxy;
import bg.duosoft.ipas.rest.custommodel.search.RSearchResponse;
import bg.duosoft.ipas.rest.model.search.RSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchRestClientImpl extends RestClientBaseImpl implements SearchRestClient {
    @Autowired
    private SearchRestProxy searchRestProxy;

    @Override
    public RSearchResponse searchIpObjects(RSearchParam rq) {
        return callService(rq, searchRestProxy::getDocumentContent);
    }
}
