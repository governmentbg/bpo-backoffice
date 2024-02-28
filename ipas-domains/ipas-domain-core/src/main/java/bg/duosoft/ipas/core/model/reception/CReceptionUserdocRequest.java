package bg.duosoft.ipas.core.model.reception;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CReceptionUserdocRequest implements Serializable {
    private Integer id;
    private String docOri;
    private String docLog;
    private Integer docSer;
    private Integer docNbr;
    private String docSeqTyp;
    private Integer docSeqNbr;
    private Integer docSeqSer;
    private String userdocType;
    private Integer externalId;
    private String externalSystemId;
    private Boolean originalExpected;
    private Date filingDate;
    private Date createDate;
    private String notes;
    private String relatedObjectSeq;
    private String relatedObjectTyp;
    private Integer relatedObjectSer;
    private Integer relatedObjectNumber;
    private CSubmissionType submissionType;
    private List<CReceptionCorrespondent> correspondents;
}
