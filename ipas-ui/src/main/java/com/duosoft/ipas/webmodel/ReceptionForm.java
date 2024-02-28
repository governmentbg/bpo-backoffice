package com.duosoft.ipas.webmodel;

import bg.duosoft.ipas.core.model.file.COwnershipData;
import bg.duosoft.ipas.core.model.file.CRepresentationData;
import bg.duosoft.ipas.core.model.reception.CReceptionUserdoc;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ReceptionForm {
    private String name;
    private Integer submissionType;
    private Integer receptionType;
    private Boolean originalExpected;
    private Date entryDate;
    private CRepresentationData representationData;
    private COwnershipData ownershipData;
    private ReceptionUserdocForm userdoc;
    private ReceptionEuPatentForm euPatent;
    private ReceptionMarkForm mark;
    private String notes;
    private ReceptionDocflowDocumentForm docflowDocument;
    private ReceptionDocflowEmailDocumentForm docflowEmailDocument;
    private List<String> additionalUserdocs;
    private InitialDocumentForm initialDocument;


    public CRepresentationData getRepresentationData() {
        if (this.representationData == null)
            this.representationData = new CRepresentationData();

        return representationData;
    }

    public COwnershipData getOwnershipData() {
        if (this.ownershipData == null)
            this.ownershipData = new COwnershipData();

        return ownershipData;
    }
    public List<ReceptionDocflowAttachment> getAttachments() {
        return docflowDocument == null ? (docflowEmailDocument == null ? (initialDocument == null ? null: initialDocument.getAttachments()) : docflowEmailDocument.getAttachments()) : docflowDocument.getAttachments();
    }
}
