package bg.duosoft.ipas.core.model.userdoc.grounds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CUserdocRootGrounds implements Serializable {
     private Integer rootGroundId;
     private String motives;
     private String groundCommonText;
     private CEarlierRightTypes earlierRightType;
     private CApplicantAuthority applicantAuthority;
     private List<CUserdocSubGrounds> userdocSubGrounds;
     private CMarkGroundData markGroundData;
     private CPatentGroundData patentGroundData;
     private List<CSingleDesignGroundData> singleDesignGroundData;
}
