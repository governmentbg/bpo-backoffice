package bg.duosoft.ipas.core.service.file;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CFileRecordal;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;
import bg.duosoft.ipas.persistence.model.entity.ext.core.IpFileRecordal;

import java.util.Date;

public interface FileRecordalService {

    CFileRecordal insertInvalidationOfRecordal(CDocumentId documentId, CActionId actionId, Date invalidationDate);

    CFileRecordal insertNewRecordal(CDocumentId documentId, CActionId actionId, Date recordalDate);

    CFileRecordal updateRecordal(CFileRecordal fileRecordal);

    CFileRecordal selectRecordal(CDocumentId documentId, CFileId fileId);

    void syncUserdocEffectiveAndInvalidationDate(CUserdoc userdoc);

    CFileRecordal selectRecordalByActionId(CActionId id);

    CFileRecordal selectRecordalByUserdocId(CDocumentId id);

    CFileRecordal selectRecordalByInvalidationUserdocId(CDocumentId id);

}
