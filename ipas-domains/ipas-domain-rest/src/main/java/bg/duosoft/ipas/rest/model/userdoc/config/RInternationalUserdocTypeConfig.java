package bg.duosoft.ipas.rest.model.userdoc.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RInternationalUserdocTypeConfig implements Serializable {
    private String userdocTyp;
    private Boolean changeMarkOwner;
    private Boolean changeMarkRepresentative;
    private Boolean changeMarkRenewalExpirationDate;
    private Boolean insertMarkActionProtectionSurrender;
    private Boolean insertMarkActionCancellation;
    private Boolean insertMarkActionNonRenewalOfTrademark;
    private Boolean insertMarkActionNonRenewalOfContractingParty;
    private Boolean internationalRegistration;
    private String generateUserdocType;
}
