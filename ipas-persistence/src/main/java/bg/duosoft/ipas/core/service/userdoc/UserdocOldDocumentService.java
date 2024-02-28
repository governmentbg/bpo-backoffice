package bg.duosoft.ipas.core.service.userdoc;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdocOldDocument;

import java.util.List;

public interface UserdocOldDocumentService {
    List<CUserdocOldDocument> selectAll();
    CReceptionResponse insertOldUserdoc(CUserdocOldDocument oldDocument, Document abdocsDocument, CFileId parentFileId, Boolean registerInAbdocs);
}
