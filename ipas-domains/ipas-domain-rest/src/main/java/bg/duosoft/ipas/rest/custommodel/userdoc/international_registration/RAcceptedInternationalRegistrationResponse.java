package bg.duosoft.ipas.rest.custommodel.userdoc.international_registration;

import bg.duosoft.ipas.rest.model.reception.RReceptionResponse;
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
public class RAcceptedInternationalRegistrationResponse {
    private RReceptionResponse receptionResponse;
}
