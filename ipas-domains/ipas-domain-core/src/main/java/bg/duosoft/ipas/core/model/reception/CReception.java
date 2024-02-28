package bg.duosoft.ipas.core.model.reception;

import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.util.CAttachment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class CReception implements Serializable {
    private Integer docflowSystemId;//if it's set, the document will not be registered in the docflow system, but the presented docflow number will be used!
    @NotNull
    private String docOri;
    private Integer submissionType;
    private Boolean originalExpected;
    private Date entryDate;
    private CRepresentationData representationData;
    private COwnershipData ownershipData;
    private CReceptionUserdoc userdoc;
    private CReceptionEuPatent euPatent;
    private CReceptionFile file;
    private String externalSystemId;
    private String notes;
    private Integer docflowEmailId;
    private Integer initialDocumentId;

    /**
     * defines if the reception document will be registered in the docflow system. If the flag is false, the document is not registered in the docflow system
     */
    private boolean registerInDocflowSystem;

    /**
     * defines if the reception request will be registered in the RECEPTION_REQUEST (RECEPTION_USERDOC_REQUEST) tables
     */
    private boolean registerReceptionRequest;

    /**
     * if provided, this documentSequenceId will be used to file the reception
     */
    private CDocumentSeqId documentSeqId;

    private List<CAttachment> attachments;

    private List<CReception> userdocReceptions;

    /**
     * False: Document will be registered in docflow system using currently logged user
     * True: Document will be registered in docflow system using administrator user
     * Default: False
     */
    private boolean registerAsAdministrator;

    public CReception() {
        docOri = "BG";
        registerReceptionRequest = true;
    }

    public boolean isUserdocRequest() {
        return userdoc != null;
    }

    public boolean isEuPatentRequest() {
        return euPatent != null;
    }

    public boolean isFileRequest() {
        return file != null && !isEuPatentRequest();//the euPatents might have file data too, so the file request should not have euPatentRequest!
    }
}
