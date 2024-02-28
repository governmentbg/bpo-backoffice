package bg.duosoft.ipas.core.model.file;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
public class CRegistrationData
        implements Serializable {
    private static final long serialVersionUID = -4299888560823250929L;
    private Boolean indRegistered;
    private Date registrationDate;
    private Date entitlementDate;
    private Date expirationDate;
    private CRegistrationId registrationId;

}


