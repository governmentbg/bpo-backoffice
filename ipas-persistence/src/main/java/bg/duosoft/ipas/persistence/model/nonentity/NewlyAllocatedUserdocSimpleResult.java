package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewlyAllocatedUserdocSimpleResult {
    private CDocumentId documentId;
    private Date filingDate;
    private String externalSystemId;
    private String userdocTypeName;
    private UserdocIpObject userdocIpObject;
    private String responsibleUserName;
    private Date dateChanged;
}
