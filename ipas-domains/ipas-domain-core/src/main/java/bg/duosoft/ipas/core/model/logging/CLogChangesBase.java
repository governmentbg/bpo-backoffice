package bg.duosoft.ipas.core.model.logging;

import bg.duosoft.ipas.core.model.person.CUser;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * User: ggeorgiev
 * Date: 03.12.2021
 * Time: 13:59
 */
@Data
public class CLogChangesBase implements Serializable {
    private Date changeDate;
    private String dataCode;
    private String dataVersionCode;
    private String dataValue;
    private int changeNumber;
    private CUser changeUser;
    private Map<String, CLogChangeDetail> changeDetails;

    public boolean isSingleChangeDetail() {
        return changeDetails.size() == 0;
    }
}
