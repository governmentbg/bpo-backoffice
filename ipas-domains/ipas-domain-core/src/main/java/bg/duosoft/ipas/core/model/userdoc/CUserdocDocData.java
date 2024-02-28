package bg.duosoft.ipas.core.model.userdoc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CUserdocDocData implements Serializable {
    private String externalSystemId;
    private Date filingDate;
    private String statusName;
}
