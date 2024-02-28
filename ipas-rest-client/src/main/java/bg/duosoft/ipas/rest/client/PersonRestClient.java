package bg.duosoft.ipas.rest.client;

import bg.duosoft.ipas.rest.model.person.RPerson;

/**
 * User: Georgi
 * Date: 21.7.2020 г.
 * Time: 15:40
 */
public interface PersonRestClient {
    RPerson getPerson(int personNbr, int addressNbr);
}
