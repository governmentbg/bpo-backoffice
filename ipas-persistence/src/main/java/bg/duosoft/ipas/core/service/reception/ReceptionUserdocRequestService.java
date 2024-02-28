package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdocRequest;
import bg.duosoft.ipas.persistence.model.nonentity.UserdocSimpleResult;
import bg.duosoft.ipas.util.filter.ReceptionUserdocListFilter;

import java.util.List;
import java.util.Map;

public interface ReceptionUserdocRequestService {

    CReceptionUserdocRequest selectReceptionByDocumentId(String docOri, String docLog, Integer docSer, Integer docNbr);

    void saveUserdocReceptionRequestRecord(Integer externalId, String externalSystemId, CReception receptionForm, CReceptionResponse receiveUserdocResponse, CFileId euPatentFileId);

    List<CReceptionUserdocRequest> selectOriginalExpectedUserdoc(String relatedObject, String userdocType);

    void updateUserdocSubmissionType(Integer submissionType, CDocumentId documentId);

    List<UserdocSimpleResult> selectUserdocReceptions(ReceptionUserdocListFilter filter);

    int selectUserdocReceptionsCount(ReceptionUserdocListFilter filter);

    Map<String, String> getUserdocStatuses(ReceptionUserdocListFilter filter);

    Map<String, String> getUserdocObjectTypes(ReceptionUserdocListFilter filter);

    Map<String, String> getUserdocTypes(ReceptionUserdocListFilter filter);

}
