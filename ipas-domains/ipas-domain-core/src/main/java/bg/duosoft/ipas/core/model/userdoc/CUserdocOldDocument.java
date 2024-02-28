package bg.duosoft.ipas.core.model.userdoc;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CUserdocOldDocument implements Serializable {
    private Integer id;
    private String fileSeq;
    private String fileType;
    private Integer fileSeries;
    private Integer fileNbr;
    private String externalSystemId;
    private Date filingDate;
    private String newUserdocType;
    private Integer responsibleUserId;
    private Boolean registerInAbdocs;
}
