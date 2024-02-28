package bg.duosoft.ipas.rest.custommodel.userdoc.international_mark;


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
public class RAcceptInternationalUserdocRequest {
    private String parentDocumentNumber;
    private Integer affectedRegistrationNbr;
    private String affectedRegistrationDup;
    private String transactionCode;
    private String trantyp;
    private RUserdoc userdoc;
}
