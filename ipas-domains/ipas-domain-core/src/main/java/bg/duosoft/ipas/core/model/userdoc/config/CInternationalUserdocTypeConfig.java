package bg.duosoft.ipas.core.model.userdoc.config;


import lombok.Data;

import java.io.Serializable;

@Data
public class CInternationalUserdocTypeConfig implements Serializable {
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
