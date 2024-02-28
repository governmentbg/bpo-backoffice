package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.document.CDoc;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocPerson;
import bg.duosoft.ipas.enums.UserdocPersonRole;

import java.util.List;

public interface UserdocPersonService {

    CUserdocPerson savePerson(CDocumentId documentId, CUserdocPerson person);

    List<CUserdocPerson> savePersonList(CDocumentId documentId, List<CUserdocPerson> personList);

    long count();

    int countByRole(CDocumentId documentId, UserdocPersonRole role);
}
