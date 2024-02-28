package bg.duosoft.ipas.rest.model.person;

import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import bg.duosoft.ipas.enums.*;
import java.io.Serializable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPerson implements Serializable {
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
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
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
}

