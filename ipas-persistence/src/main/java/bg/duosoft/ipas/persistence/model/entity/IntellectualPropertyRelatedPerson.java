package bg.duosoft.ipas.persistence.model.entity;

import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;

/**
 * User: ggeorgiev
 * Date: 25.3.2019 г.
 * Time: 18:33
 */
public interface IntellectualPropertyRelatedPerson {
    IntellectualPropertyRelatedPersonPK getPk();

    IpPersonAddresses getIpPersonAddresses();
    void setIpPersonAddresses(IpPersonAddresses pa);
}
