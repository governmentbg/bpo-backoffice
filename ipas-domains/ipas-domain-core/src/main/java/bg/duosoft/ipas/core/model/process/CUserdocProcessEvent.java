package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.Data;

import java.util.Date;

@Data
public class CUserdocProcessEvent implements java.io.Serializable {
    private static final long serialVersionUID = -4712235627737660629L;
    private Date filingDate;
    private String notes;
    private String userdocName;
    private String status;
    private CProcessId userdocProcessId;
    private CDocumentId documentId;
    private CDocumentSeqId documentSeqId;
    private CProcessId upperProcessId;
    private String applicantName;
    private String externalSystemId;
    private Date expirationDate;
    private CNextProcessAction nextProcessActionAfterExpirationDate;
}