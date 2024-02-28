package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IPObjectSimpleResult {
    private String fileSeq;
    private String fileType;
    private Integer fileSer;
    private Integer fileNum;
    private String registrationNumber;
    private String title;
    private String statusCode;
    private String statusName;
    private Date filingDate;
    private Date expirationDate;
    private Date actionDate;
    private String filingNumber;
    private String actionName;
    private Date statusDate;
    private String actionType;
    private String responsibleUserName;
    private String transactionType;
    private Date receptionDate;
    private String procId;
    private String newlyAllocated;
    private String priorityRequest;
    private String  registrationDup;
    private String bordero;
    private String journalCode;
}