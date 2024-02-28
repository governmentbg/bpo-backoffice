package bg.duosoft.ipas.core.model.person;

import bg.duosoft.ipas.enums.GralPersonIdType;
import bg.duosoft.ipas.enums.PartnershipType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@ToString
public class CPerson
        implements Serializable {
    private static final long serialVersionUID = 1881533257442633505L;
    private Integer personNbr;
    private Integer addressNbr;
    private String personName;
    private String nationalityCountryCode;
    private Boolean indCompany;
    private String legalNature;
    private String legalIdType;
    private String legalIdNbr;
    private String individualIdType;
    private Integer individualIdNbr;
    private String individualIdTxt;
    private String agentCode;
    private Boolean isAgentInactive;
    private PartnershipType partnershipType;
    private String stateName;
    private String cityName;
    private String addressZone;
    private String addressStreet;
    private String zipCode;
    private String email;
    private String telephone;
    private Date companyRegisterRegistrationDate;
    private Long companyRegisterRegistrationNbr;
    private String residenceCountryCode;
    private String stateCode;
    private String cityCode;
    private String personGroupCode;
    private String personGroupName;
    private String personNameInOtherLang;
    private String legalNatureInOtherLang;
    private String addressStreetInOtherLang;
    private Integer gralPersonIdNbr;
    private String gralPersonIdTyp;
    private Integer tempParentPersonNbr;
    private Integer personLastVersion;

    public boolean hasAgentCode() {
        return Objects.nonNull(this.agentCode) && (!this.agentCode.equals(""));
    }

    public boolean isLastVersion() {
        return Objects.nonNull(this.gralPersonIdTyp) && (this.gralPersonIdTyp.equals(GralPersonIdType.LAST.name()));
    }

    public boolean isOldVersion() {
        return Objects.nonNull(this.gralPersonIdTyp) && (this.gralPersonIdTyp.equals(GralPersonIdType.OLD.name()));
    }

    public boolean isForeigner(){
        return Objects.nonNull(this.nationalityCountryCode) && !(this.nationalityCountryCode.equalsIgnoreCase("BG"));
    }

    public boolean isBulgarianNatinality(){
        return Objects.nonNull(this.nationalityCountryCode) && (this.nationalityCountryCode.equalsIgnoreCase("BG"));
    }

    public boolean isCompany(){
        return Objects.nonNull(this.indCompany) && this.indCompany;
    }

    public boolean isPhysicalPerson(){
        return Objects.nonNull(this.indCompany) && (!this.indCompany);
    }
}


