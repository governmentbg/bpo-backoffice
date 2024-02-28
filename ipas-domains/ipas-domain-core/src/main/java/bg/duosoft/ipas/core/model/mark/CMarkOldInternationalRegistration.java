package bg.duosoft.ipas.core.model.mark;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CMarkOldInternationalRegistration implements Serializable {
    private Integer id;
    private Integer wipoReference;
    private String bgReference;
    private Date receivedDate;
    private Integer fileNbr;
    private Integer registrationNbr;
    private String registrationDup;
    private String intlRegNbr;
    private Date intlRegDate;
    private String holderName;
    private String holderAddress;
    private Date processedDate;
    private String externalSystemId;
    private Boolean hasAbdocsRecord;
}
