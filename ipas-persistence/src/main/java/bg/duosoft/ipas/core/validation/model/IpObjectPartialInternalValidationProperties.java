package bg.duosoft.ipas.core.validation.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IpObjectPartialInternalValidationProperties {

    public static IpObjectPartialInternalValidationProperties getInstance() {
        return new IpObjectPartialInternalValidationProperties(true);
    }

    private IpObjectPartialInternalValidationProperties(boolean isInternalValidation) {
        this.isInternalValidation = isInternalValidation;
    }

    private boolean isInternalValidation;
}
