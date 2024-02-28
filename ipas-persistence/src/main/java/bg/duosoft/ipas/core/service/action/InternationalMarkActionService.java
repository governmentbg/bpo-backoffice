package bg.duosoft.ipas.core.service.action;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.userdoc.CUserdoc;

public interface InternationalMarkActionService {
    void insertMarkFirstNormalAction(CMark insertedMark, boolean isFullyDivided);

    void insertSpecialAction(CDocumentId documentId);

    void insertUserdocNormalAction(CUserdoc userdoc, String extParamCode);
}
