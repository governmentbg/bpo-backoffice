package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.process.CProcess;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserdocSimpleResult {
    private CDocumentId documentId;
    private Date filingDate;
    private String submissionType;
    private Boolean originalExpected;
    private String notes;
    private String externalSystemId;
    private String statusCode;
    private String statusName;
    private String userdocType;
    private String userdocTypeName;
    private UserdocIpObject userdocIpObject;
    private String responsibleUserName;
    private Date expirationDate;
    private Date createDate;
    private String procId;
    private String newlyAllocated;
}