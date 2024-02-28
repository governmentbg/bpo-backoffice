package bg.duosoft.ipas.core.model.mark;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CRenewalData implements Serializable {

    private Date lastRenewalDate;

}


