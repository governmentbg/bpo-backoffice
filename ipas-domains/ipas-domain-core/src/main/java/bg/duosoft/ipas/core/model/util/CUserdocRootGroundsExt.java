package bg.duosoft.ipas.core.model.util;

import bg.duosoft.ipas.core.model.userdoc.grounds.CUserdocRootGrounds;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CUserdocRootGroundsExt extends CUserdocRootGrounds {
    private boolean nameDataChanged;
    public void initGroundsExt(CUserdocRootGrounds rootGround){
        this.setRootGroundId(rootGround.getRootGroundId());
        this.setMotives(rootGround.getMotives());
        this.setGroundCommonText(rootGround.getGroundCommonText());
        this.setEarlierRightType(rootGround.getEarlierRightType());
        this.setApplicantAuthority(rootGround.getApplicantAuthority());
        this.setUserdocSubGrounds(rootGround.getUserdocSubGrounds());
        this.setMarkGroundData(rootGround.getMarkGroundData());
     }
}
