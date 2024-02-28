package bg.duosoft.ipas.core.model.util;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@EqualsAndHashCode
public class UserDocSearchResult implements Serializable {
    private CDocumentId documentId;
    private CFileId fileId;
    private Date filingDate;
    private String docType;
    private String docNumber;
    private List<String> applicants;
    private CProcessId processId;
    private String status;
    private String fileTitle;
    private String fileRegistrationNumber;


    public UserDocSearchResult fileId(CFileId fileId) {
        this.fileId = fileId;
        return this;
    }

    public UserDocSearchResult documentId(CDocumentId documentId) {
        this.documentId = documentId;
        return this;
    }

    public UserDocSearchResult filingDate(Date filingDate) {
        this.filingDate = filingDate;
        return this;
    }

    public UserDocSearchResult docType(String docType) {
        this.docType = docType;
        return this;
    }

    public UserDocSearchResult docNumber(String docNumber) {
        this.docNumber = docNumber;
        return this;
    }

    public UserDocSearchResult applicants(List<String> applicants) {
        this.applicants = applicants;
        return this;
    }

    public UserDocSearchResult processId(CProcessId processId) {
        this.processId = processId;
        return this;
    }
    public UserDocSearchResult status(String status) {
        this.status = status;
        return this;
    }


    public UserDocSearchResult fileTitle(String title) {
        this.fileTitle = title;
        return this;
    }
    public UserDocSearchResult fileRegistrationNumber(String fileRegistrationNumber) {
        this.fileRegistrationNumber = fileRegistrationNumber;
        return this;
    }
}
