package bg.duosoft.ipas.core.service.person;

import bg.duosoft.ipas.core.model.person.CPersonIdType;

public interface PersonIdTypeService {

    CPersonIdType selectById(String type);

}
