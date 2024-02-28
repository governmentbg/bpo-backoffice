package bg.duosoft.ipas.core.service.efiling;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;

public interface UserdocEfilingDataService {
    void updateLogUserName(String externalSystemId,String username);

    boolean doesRecordExist(CDocumentId documentId);

    CEFilingData selectById(CDocumentId documentId);

}
