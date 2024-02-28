package bg.duosoft.ipas.core.service.userdoc.config;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import bg.duosoft.ipas.core.model.userdoc.config.CUserdocTypeConfig;

public interface UserdocTypeConfigService {
    CUserdocTypeConfig findById(String userdocType);
    void defineUpperProc(CReceptionUserdoc userdocReception, CFileId affectedId);
    void defineResponsibleUserOnReception(CDocumentId docId,Integer submissionType);
    void defineResponsibleUserOnRelocation(CDocumentId docId, CProcessId userdocProcessId, CProcessId newUpperProcessId);
}
