package bg.duosoft.ipas.core.model.process;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class CProcessAuthorizationData implements Serializable {

    //Common
    private Date effectiveDate;
    private Date invalidationDate;

    //Change representative
    private Boolean transferCorrespondenceAddress;

}
