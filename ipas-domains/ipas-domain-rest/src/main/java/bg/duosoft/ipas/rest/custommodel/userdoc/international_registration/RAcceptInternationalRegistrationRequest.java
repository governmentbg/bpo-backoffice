package bg.duosoft.ipas.rest.custommodel.userdoc.international_registration;

import bg.duosoft.ipas.rest.model.userdoc.RUserdocExtraData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAcceptInternationalRegistrationRequest {
    private String registrationRequestExternalSystemId;
    private Integer wipoReference;
    private List<RUserdocExtraData> userdocExtraData;

    public List<RUserdocExtraData> getUserdocExtraData() {
        if (userdocExtraData == null) {
            userdocExtraData = new ArrayList<>();
        }
        return userdocExtraData;
    }
}
