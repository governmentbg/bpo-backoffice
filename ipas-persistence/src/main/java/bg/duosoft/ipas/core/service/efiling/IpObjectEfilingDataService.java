package bg.duosoft.ipas.core.service.efiling;

import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFileId;


public interface IpObjectEfilingDataService {
    void updateLogUserName(CFileId cFileId,String username);

    boolean doesRecordExist(CFileId fileId);

    CEFilingData selectById(CFileId fileId);

}
