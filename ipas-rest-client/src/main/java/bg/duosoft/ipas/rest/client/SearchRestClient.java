package bg.duosoft.ipas.rest.client;


import bg.duosoft.ipas.rest.custommodel.search.RSearchResponse;
import bg.duosoft.ipas.rest.model.search.RSearchParam;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:29
 */
public interface SearchRestClient {
    RSearchResponse searchIpObjects(RSearchParam rq);
}
