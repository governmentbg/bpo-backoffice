package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;

public interface UserdocMadridEfilingService {
    CReceptionResponse acceptMadridEfilingUserdoc(String applicationReference, CFileId affectedId, CUserdoc userdoc);
}
