package bg.duosoft.ipas.rest.custommodel.userdoc.international_registration;

import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.userdoc.RUserdoc;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RAcceptMadridEfilingUserdocRequest {
    private RFileId affectedId;
    private Integer affectedRegistrationNbr;
    private String affectedRegistrationDup;
    private String applicationReference;
    private RUserdoc userdoc;
}
