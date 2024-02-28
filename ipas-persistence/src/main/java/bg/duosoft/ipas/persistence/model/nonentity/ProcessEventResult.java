package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProcessEventResult {
    private CProcessId processId;
    private CProcessId upperProcessId;
    private CFileId fileId;
    private CDocumentId documentId;
    private COffidocId offidocId;
    private Date processCreationDate;
    private Date processExpirationDate;
    private String statusCode;
    private String userdocTyp;
    private Date documentFilingDate;
    private CDocumentSeqId documentSeqId;
    private String externalSystemId;
    private String applicantName;
    private String userdocNotes;
    private String responsibleUserName;
}
