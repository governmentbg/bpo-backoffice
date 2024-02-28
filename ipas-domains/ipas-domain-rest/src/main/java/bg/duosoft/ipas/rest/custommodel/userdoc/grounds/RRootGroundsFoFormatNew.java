package bg.duosoft.ipas.rest.custommodel.userdoc.grounds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRootGroundsFoFormatNew {
    private Integer rootGroundId;
    private String motives;
    private String legalActVersion;
    private List<String> articleReferences;
    private String earlierEntitlementRightType;
    private String commonText;
    private String opponentEntitlementKind;
    private RMarkGroundDataFoFormat markGroundDataFoFormat;
    private RPatentGroundDataFoFormat patentGroundDataFoFormat;
    private List<RSingleDesignGroundDataFoFormat> singleDesignGroundDataFoFormat;
}
