package bg.duosoft.ipas.core.model.document;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CDocument implements Serializable {
    private static final long serialVersionUID = -7920381263936029193L;
    private Integer rowVersion;
    private Date filingDate;
    private String receptionWcode;
    private CDocumentId documentId;
    private Long externalOfficeCode;
    private Date externalOfficeFilingDate;
    private String externalSystemId;
    private Boolean indNotAllFilesCapturedYet;
    private String notes;
    private String applSubtyp;
    private Date receptionDate;
    private CDocumentSeqId documentSeqId;
    private Integer receptionUserId;
    private Boolean indFaxReception;
    private CExtraData extraData;
    private CFileId fileId;
    private Date dailyLogDate;
    private String appType;
    private boolean isUserdoc;
    private CUserdocType cUserdocType;
}


