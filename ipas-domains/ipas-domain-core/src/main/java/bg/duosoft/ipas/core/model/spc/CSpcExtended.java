package bg.duosoft.ipas.core.model.spc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CSpcExtended implements Serializable {

    private static final long serialVersionUID = -4985662158486518798L;
    private String bgPermitNumber;
    private Date bgPermitDate;
    private String euPermitNumber;
    private Date euPermitDate;
    private String productClaims;
    private Date bgNotificationDate;
    private Date euNotificationDate;
}
