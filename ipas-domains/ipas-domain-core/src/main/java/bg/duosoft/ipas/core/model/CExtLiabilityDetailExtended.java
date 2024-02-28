package bg.duosoft.ipas.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class CExtLiabilityDetailExtended extends CExtLiabilityDetail {
    private String externalSystemId;
    public String getExternalSystemIdOrFilingNumber() {
        return externalSystemId == null || "".equals(externalSystemId) ? createFilingNumber() : externalSystemId;
    }
}
