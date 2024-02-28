package bg.duosoft.ipas.persistence.model.nonentity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class IPMarkSimpleResult extends IPObjectSimpleResult {
    private String allNiceClasses;

    public IPMarkSimpleResult(String fileSeq, String fileType, Integer fileSer, Integer fileNum, String registrationNumber, String title, String statusCode, String statusName, Date filingDate, Date expirationDate, Date actionDate, String filingNumber, String actionName, Date statusDate, String actionType, String responsibleUserName, String transactionType, Date receptionDate, String procId, String newlyAllocated, String priorityRequest, String registrationDup, String bordero, String journalCode, String allNiceClasses) {
        super(fileSeq, fileType, fileSer, fileNum, registrationNumber, title, statusCode, statusName, filingDate, expirationDate, actionDate, filingNumber, actionName, statusDate, actionType, responsibleUserName, transactionType, receptionDate, procId, newlyAllocated, priorityRequest, registrationDup, bordero, journalCode);
        this.allNiceClasses = allNiceClasses;
    }
}
