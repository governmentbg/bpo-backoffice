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
public class RMarkGroundDataFoFormat {
    private String groundCategoryCode;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date registrationDate;
    private String registrationNbr;
    private String regCountry;
    private String geographicalIndicationTypeDescription;
    private String markSignTypeDescription;
    private String filingNumber;
    private Boolean markImported;
    private String nameText;
    private byte[] nameData;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date filingDate;
    private Integer markGroundType;
    private Boolean niceClassesInd;
    private List<RGroundNiceClasses> userdocGroundsNiceClasses;
}
