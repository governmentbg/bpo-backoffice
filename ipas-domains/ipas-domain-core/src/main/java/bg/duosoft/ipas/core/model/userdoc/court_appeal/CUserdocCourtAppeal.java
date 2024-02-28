package bg.duosoft.ipas.core.model.userdoc.court_appeal;

import bg.duosoft.ipas.core.model.miscellaneous.CCourt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CUserdocCourtAppeal  implements Serializable {
    private Integer courtAppealId;
    private String courtCaseNbr;
    private Date courtCaseDate;
    private String judicialActNbr;
    private Date judicialActDate;
    private String courtLink;
    private CCourt court;
    private CJudicialActType judicialActType;
}
