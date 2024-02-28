package bg.duosoft.ipas.core.service.offidoc;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.offidoc.COffidoc;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import bg.duosoft.ipas.core.model.process.CProcessEvent;
import bg.duosoft.ipas.services.core.IpasServiceException;

import java.util.List;
import java.util.Map;

public interface OffidocService {

    String OFFIDOC_DIRECTION_OUT = "O";
    String OFFIDOC_DIRECTION_IN = "I";

    COffidoc findOffidoc(String offidocOri, Integer offidocSer, Integer offidocNbr);

    COffidoc findOffidoc(COffidocId cOffidocId);

    String findOffidocType(COffidocId cOffidocId);

    boolean printOffidoc(COffidocId cOffidocId) throws IpasServiceException;

    byte[] generateOffidocDocument(String reportPath, CActionId cActionId) throws IpasServiceException;

    Map<String, byte[]> generateOffidocForProcessEvent(CProcessEvent processEvent, List<String> offidocTemplates) throws IpasServiceException;

    Map<String, byte[]> generateOffidocFilesFromTemplate(CActionId actionId, List<String> offidocTemplates) throws IpasServiceException;



}
