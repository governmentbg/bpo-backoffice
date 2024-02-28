package bg.duosoft.ipas.core.model.file;

import bg.duosoft.ipas.core.model.document.CDocument;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class CFilingData implements java.io.Serializable {
    private static final long serialVersionUID = -5008554642342614215L;

    @NotNull
    private String applicationType;

    @NotNull
    private String applicationSubtype;

    @NotNull
    private Integer lawCode;

    @NotNull
    private Date filingDate;

    @NotNull
    private Date receptionDate;

    private Date captureDate;

    private Long captureUserId;

    private Date validationDate;

    private Long validationUserId;

    private String externalOfficeCode;

    private Date externalOfficeFilingDate;

    private String externalSystemId;

    private Boolean indManualInterpretationRequired;

    private Date novelty1Date;

    private Date novelty2Date;

    private Long receptionUserId;

    private String corrFileType;

    private Long corrFileSeries;

    private String corrFileSeq;

    private Long corrFileNbr;

    private String publicationTyp;

    private String indIncorrRecpDeleted;

    private CDocument receptionDocument;

    private List<CUserdocType> userdocTypeList;

}


