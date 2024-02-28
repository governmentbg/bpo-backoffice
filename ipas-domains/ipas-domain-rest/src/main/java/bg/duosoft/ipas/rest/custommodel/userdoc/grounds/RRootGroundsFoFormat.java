package bg.duosoft.ipas.rest.custommodel.userdoc.grounds;

import bg.duosoft.ipas.rest.model.userdoc.grounds.RGroundNiceClasses;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRootGroundsFoFormat {
    private Integer rootGroundId;
    private String motives;
    private String legalActVersion;
    private List<String> articleReferences;
    private String groundCategoryCode;
    private String earlierEntitlementRightType;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date registrationDate;
    private String registrationNbr;
    private String regCountry;
    private String commonText;
    private String geographicalIndicationTypeDescription;
    private String markSignTypeDescription;
    private String opponentEntitlementKind;
    private String filingNumber;
    private Boolean markImported;
    private String nameText;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date filingDate;
    private Integer markGroundType;
    private Boolean niceClassesInd;
    private List<RGroundNiceClasses> userdocGroundsNiceClasses;
}