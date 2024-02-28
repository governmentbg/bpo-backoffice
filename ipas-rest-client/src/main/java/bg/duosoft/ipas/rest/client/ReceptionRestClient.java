package bg.duosoft.ipas.rest.client;

import bg.duosoft.ipas.rest.model.reception.RReception;
import bg.duosoft.ipas.rest.model.reception.RReceptionResponse;

/**
 * User: Georgi
 * Date: 11.9.2020 г.
 * Time: 9:44
 */
public interface ReceptionRestClient {
    RReceptionResponse createReception(RReception request);
}
