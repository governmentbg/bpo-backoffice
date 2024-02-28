package bg.duosoft.ipas.core.model.mark;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CMadridApplicationData
        implements Serializable {
    private static final long serialVersionUID = 7359047591538366606L;
    private String internationalFileNumber;
    private String basicFileRef;
    private Date intFilingDate;

}


