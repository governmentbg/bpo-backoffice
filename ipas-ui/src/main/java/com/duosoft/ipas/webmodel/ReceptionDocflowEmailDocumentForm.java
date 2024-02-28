package com.duosoft.ipas.webmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * User: Georgi
 * Date: 23.10.2020 г.
 * Time: 18:39
 */
@Data
@AllArgsConstructor
public class ReceptionDocflowEmailDocumentForm {
    private int emailId;
    private String subject;
    private List<ReceptionDocflowAttachment> attachments;
    private String emailAddress;
}
