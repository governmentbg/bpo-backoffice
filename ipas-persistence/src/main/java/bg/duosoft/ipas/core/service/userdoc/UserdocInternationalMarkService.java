package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;

public interface UserdocInternationalMarkService {
    CReceptionResponse acceptInternationalMarkUserdoc(String parentDocumentNumber, CFileId affectedId, CUserdoc userdoc, String transaction, String trantyp);
}
