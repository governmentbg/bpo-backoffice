package com.duosoft.ipas.webmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * User: Georgi
 * Date: 16.10.2020 г.
 * Time: 14:55
 */
@Data
@AllArgsConstructor
public class ReceptionDocflowDocumentForm {
    private int documentId;
    private String subject;
    private String regUri;
    private String emailAddress;
    private List<ReceptionDocflowAttachment> attachments;
}
