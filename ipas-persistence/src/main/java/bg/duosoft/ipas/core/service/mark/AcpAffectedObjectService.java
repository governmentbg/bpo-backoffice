package bg.duosoft.ipas.core.service.mark;

import bg.duosoft.ipas.core.model.acp.CAcpAffectedObject;
import bg.duosoft.ipas.core.model.file.CFileId;

public interface AcpAffectedObjectService {
    CAcpAffectedObject constructAffectedObjectByFileId (CFileId fileId);
}
