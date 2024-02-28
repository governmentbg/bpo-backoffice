package bg.duosoft.ipas.persistence.model.entity.nomenclature;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "CF_INTERNATIONAL_USERDOC_TYPE_CONFIG", schema = "EXT_CORE")
@Cacheable(value = false)
public class CfInternationalUserdocTypeConfig implements Serializable {
    @Id
    @Column(name = "USERDOC_TYP")
    private String userdocTyp;

    @Column(name = "CHANGE_MARK_OWNER")
    private String changeMarkOwner;

    @Column(name = "CHANGE_MARK_REPRESENTATIVE")
    private String changeMarkRepresentative;

    @Column(name = "CHANGE_MARK_RENEWAL_EXPIRATION_DATE")
    private String changeMarkRenewalExpirationDate;

    @Column(name = "INSERT_MARK_ACTION_PROTECTION_SURRENDER")
    private String insertMarkActionProtectionSurrender;

    @Column(name = "INSERT_MARK_ACTION_CANCELLATION")
    private String insertMarkActionCancellation;

    @Column(name = "INSERT_MARK_ACTION_NON_RENEWAL_OF_TRADEMARK")
    private String insertMarkActionNonRenewalOfTrademark;

    @Column(name = "INSERT_MARK_ACTION_NON_RENEWAL_OF_CONTRACTING_PARTY")
    private String insertMarkActionNonRenewalOfContractingParty;

    @Column(name = "INTERNATIONAL_REGISTRATION")
    private String internationalRegistration;

    @Column(name = "GENERATE_USERDOC_TYPE")
    private String generateUserdocType;
}
