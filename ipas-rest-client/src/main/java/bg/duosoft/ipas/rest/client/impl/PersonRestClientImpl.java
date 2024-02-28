package bg.duosoft.ipas.rest.client.impl;

import bg.duosoft.ipas.rest.client.PersonRestClient;
import bg.duosoft.ipas.rest.client.proxy.PersonRestProxy;
import bg.duosoft.ipas.rest.custommodel.person.RGetPersonRequest;
import bg.duosoft.ipas.rest.model.person.RPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: Georgi
 * Date: 21.7.2020 г.
 * Time: 15:42
 */
@Service
public class PersonRestClientImpl extends RestClientBaseImpl implements PersonRestClient {
    @Autowired
    private PersonRestProxy personRestProxy;
    @Override
    public RPerson getPerson(int personNbr, int addressNbr) {
        return callService(new RGetPersonRequest(personNbr, addressNbr), personRestProxy::getPerson);
    }
}
