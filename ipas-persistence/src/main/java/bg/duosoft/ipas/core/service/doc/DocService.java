package bg.duosoft.ipas.core.service.doc;

import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import org.hibernate.search.annotations.DocumentId;

import java.util.Date;

public interface DocService {

    boolean isDocumentExist(String docOri, String docLog, Integer docSer, Integer docNbr);

    default String selectExternalSystemId(CDocumentId documentId) {
        return selectExternalSystemId(documentId.getDocOrigin(), documentId.getDocLog(), documentId.getDocSeries(), documentId.getDocNbr());
    }
    String selectExternalSystemId(String docOri, String docLog, Integer docSer, Integer docNbr);

    void updateExternalSystemId(String externalSystemId, String docOri, String docLog, Integer docSer, Integer docNbr);

    CDocumentId selectDocumentIdByExternalSystemId(String externalSystemId);

    CDocumentId selectDocumentIdByExternalSystemIdAndTypeAndParentFileId(String externalSystemId, String userdocTyp, CFileId fileId);

    CDocument selectDocument(CDocumentId documentId);

    long count();
}
