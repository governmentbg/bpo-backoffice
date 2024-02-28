package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import lombok.Data;

import java.io.Serializable;

@Data
public class CProcessOriginData implements Serializable {
    private static final long serialVersionUID = -1109582360627354009L;
    private Integer relatedToWorkCode;
    private CProcessId topProcessId;
    private CProcessId upperProcessId;
    private CFileId fileId;
    private String applicationType;
    private COffidocId offidocId;
    private CDocumentId documentId;
    private CFileId userdocFileId;
    private String userdocType;
    private String manualProcDescription;
    private Integer manualProcRef;
}
