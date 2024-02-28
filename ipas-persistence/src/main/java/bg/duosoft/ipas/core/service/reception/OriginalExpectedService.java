package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.abdocs.model.Document;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;

public interface OriginalExpectedService {

    Integer updateOriginalExpectedOnReception(CReception receptionForm, Integer receptionRequestId) throws IpasValidationException;

    void updateIpObjectOriginalExpectedOnAbdocsNotification(CFileId fileId, Document document);

    void updateUserdocOriginalExpectedOnAbdocsNotification(CDocumentId userdocId, Document document);

}
