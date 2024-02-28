package com.duosoft.ipas.webmodel;


import lombok.Data;

import java.util.List;

@Data
public class InitialDocumentForm {
    private Integer documentId;
    private String subject;
    private String regNumber;
    private String description;
    private List<ReceptionDocflowAttachment> attachments;
}
