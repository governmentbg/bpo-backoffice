package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserdocIpDocSimpleResult {
    private CDocumentId documentId;
    private String registrationNumber;
}
