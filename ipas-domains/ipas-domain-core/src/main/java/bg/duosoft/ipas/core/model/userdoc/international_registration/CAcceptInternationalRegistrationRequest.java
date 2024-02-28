package bg.duosoft.ipas.core.model.userdoc.international_registration;

import bg.duosoft.ipas.core.model.userdoc.CUserdocExtraData;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CAcceptInternationalRegistrationRequest implements Serializable {
    private String registrationRequestExternalSystemId;
    private Integer wipoReference;
    private List<CUserdocExtraData> userdocExtraData;
}
