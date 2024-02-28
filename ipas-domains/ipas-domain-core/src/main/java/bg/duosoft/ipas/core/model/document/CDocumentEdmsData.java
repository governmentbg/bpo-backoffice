package bg.duosoft.ipas.core.model.document;

import lombok.Data;

import java.util.Date;

@Data
public class CDocumentEdmsData implements java.io.Serializable {
    private static final long serialVersionUID = 4437901053124270372L;
    private Long edocId;
    private String edocSeq;
    private Long edocSer;
    private Long edocNbr;
    private Date edocDate;
    private String edocTyp;
    private String edocTypeName;
    private Date edocImageLinkingDate;
    private Long edocImageLinkingUser;
    private Long efolderId;
    private String efolderSeq;
    private Long efolderSer;
    private Long efolderNbr;
    private Boolean indInterfaceEdoc;
    private Boolean indSpecificEdoc;
    private Date edocImageCertifDate;
    private Long edocImageCertifUser;

}


