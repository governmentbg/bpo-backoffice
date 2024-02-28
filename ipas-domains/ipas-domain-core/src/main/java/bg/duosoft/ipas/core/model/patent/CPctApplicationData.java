package bg.duosoft.ipas.core.model.patent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPctApplicationData implements Serializable {

    private static final long serialVersionUID = -1748808466095994026L;
    private Long pctPhase;
    private String pctApplicationId;
    private Date pctApplicationDate;
    private String pctPublicationCountryCode;
    private Date pctPublicationDate;
    private String pctPublicationType;
    private String pctPublicationId;

}
