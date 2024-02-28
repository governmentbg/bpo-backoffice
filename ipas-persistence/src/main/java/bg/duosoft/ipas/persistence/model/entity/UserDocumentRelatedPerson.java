package bg.duosoft.ipas.persistence.model.entity;

import bg.duosoft.ipas.persistence.model.entity.person.IpPersonAddresses;


public interface UserDocumentRelatedPerson {
    UserDocumentRelatedPersonPK getPk();

    IpPersonAddresses getIpPersonAddresses();

    void setIpPersonAddresses(IpPersonAddresses pa);
}
