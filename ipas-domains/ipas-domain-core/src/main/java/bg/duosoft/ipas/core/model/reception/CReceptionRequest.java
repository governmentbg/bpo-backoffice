package bg.duosoft.ipas.core.model.reception;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CReceptionRequest implements Serializable {
    private Integer id;
    private String fileSeq;
    private String fileType;
    private Integer fileSer;
    private Integer fileNbr;
    private Integer externalId;
    private CSubmissionType submissionType;
    private Boolean originalExpected;
    private Date filingDate;
    private Date createDate;
    private String name;
    private Integer status;
    private String notes;
    private List<CReceptionCorrespondent> correspondents;
}
