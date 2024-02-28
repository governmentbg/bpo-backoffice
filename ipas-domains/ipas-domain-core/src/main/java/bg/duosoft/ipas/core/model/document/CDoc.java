package bg.duosoft.ipas.core.model.document;

import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CDoc implements Serializable {

    private CDocumentId cDocumentId;
    private Date filingDate;
    private String receptionWcode;
    private String externalSystemId;
    private String notes;
    private Date receptionDate;
    private Integer docSeqNbr;
    private Integer docSeqSeries;
    private Integer receptionUserId;
    private String offidocOri;
    private Integer offidocNbr;
    private Integer offidocSer;
    private String officeDivisionCode;
    private String officeDepartmentCode;
    private String officeSectionCode;
    private CUserdocType cUserdoc;
}


