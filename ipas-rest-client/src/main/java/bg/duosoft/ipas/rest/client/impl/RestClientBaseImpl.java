package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.custommodel.RestApiRequest;
import bg.duosoft.ipas.rest.custommodel.RestApiResponse;

import java.util.function.Function;

/**
 * User: Georgi
 * Date: 20.7.2020 г.
 * Time: 16:00
 */
public class RestClientBaseImpl {
    protected  <T,V> V callService(T rq, Function<RestApiRequest<T>,RestApiResponse<V>> function) {
        RestApiRequest<T> _rq = new RestApiRequest<>(rq);
        return function.apply(_rq).getData();
    }
}
