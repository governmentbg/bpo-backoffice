package bg.duosoft.ipas.core.validation.model;

import lombok.*;

@Getter
@Setter
public class IpObjectAuthorizationValidationProperties {

    public static IpObjectAuthorizationValidationProperties getInstance() {
        return new IpObjectAuthorizationValidationProperties(true);
    }

    private IpObjectAuthorizationValidationProperties(boolean isIpObjectAuthorization) {
        this.isIpObjectAuthorization = isIpObjectAuthorization;
    }

    private boolean isIpObjectAuthorization;

}
