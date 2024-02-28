package bg.duosoft.ipas.core.model.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class PersonAddressResult implements Serializable {

    private Integer personNbr;

    private Integer addressNbr;

    private String personName;

    private String nationalityCountryCode;

    private Boolean indCompany;

    private String agentCode;

    private String cityName;

    private String addressZone;

    private String addressStreet;

    private String zipCode;

    private String email;

    private String telephone;

    private String residenceCountryCode;

    private String gralPersonIdTyp;

    private Boolean agentIndInactive;

    public PersonAddressResult personNbr(Integer personNbr) {
        this.personNbr = personNbr;
        return this;
    }

    public PersonAddressResult addressNbr(Integer addressNbr) {
        this.addressNbr = addressNbr;
        return this;
    }

    public PersonAddressResult personName(String personName) {
        this.personName = personName;
        return this;
    }

    public PersonAddressResult nationalityCountryCode(String nationalityCountryCode) {
        this.nationalityCountryCode = nationalityCountryCode;
        return this;
    }

    public PersonAddressResult indCompany(Boolean indCompany) {
        this.indCompany = indCompany;
        return this;
    }

    public PersonAddressResult agentCode(String agentCode) {
        this.agentCode = agentCode;
        return this;
    }

    public PersonAddressResult agentCode(Integer agentCode, String partnershipCode) {
        if (agentCode != null) {
            this.agentCode = agentCode.toString();
        } else if (partnershipCode != null) {
            this.agentCode = partnershipCode;
        }

        return this;
    }

    public PersonAddressResult cityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public PersonAddressResult addressZone(String addressZone) {
        this.addressZone = addressZone;
        return this;
    }

    public PersonAddressResult addressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
        return this;
    }

    public PersonAddressResult zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public PersonAddressResult email(String email) {
        this.email = email;
        return this;
    }

    public PersonAddressResult telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public PersonAddressResult residenceCountryCode(String residenceCountryCode) {
        this.residenceCountryCode = residenceCountryCode;
        return this;
    }

    public PersonAddressResult personWcode(String personWcode) {
        setPersonWcode(personWcode);
        return this;
    }
    public PersonAddressResult agentIndInactive(Integer agentCode, String partnershipCode, String indInactiveAgent, String indInactivePartnership) {
        if (indInactiveAgent != null) {
            this.agentIndInactive = "S".equals(indInactiveAgent);
        } else if (indInactivePartnership != null) {
            this.agentIndInactive = "S".equals(indInactivePartnership);
        }
        //if the person is agent (has agent code or partnershipCode), but the agentIndInactive is null, then agentIndInactive becomes false!
        if (agentIndInactive == null && (agentCode != null || (partnershipCode != null && !"".equals(partnershipCode)))) {
            agentIndInactive = false;
        }
        return this;
    }

    public Boolean getAgentIndInactive() {
        return agentIndInactive;
    }

    public String getPersonWcode() {
        return indCompany ? "M" : "F";
    }

    public void setPersonWcode(String personWcode) {
        if (!(Objects.isNull(personWcode) || "".equals(personWcode))) {
            if (personWcode.equalsIgnoreCase("M")) {
                indCompany(true);
            } else {
                indCompany(false);
            }
        }
    }

    public boolean hasAgentCode() {
        return Objects.nonNull(this.agentCode) && (!this.agentCode.equals(""));
    }

    public PersonAddressResult gralPersonIdTyp(String gralPersonIdTyp) {
        this.gralPersonIdTyp = gralPersonIdTyp;

        return this;
    }
}
