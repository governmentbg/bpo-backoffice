package bg.duosoft.ipas.core.model.efiling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CEFilingData implements Serializable {
    private String logUserName;
    private String esUser;
    private String esUserName;
    private String esUserEmail;
    private Date esValidFrom;
    private Date esValidTo;
    private Date esDate;
    private Boolean priorityRequest;
}
