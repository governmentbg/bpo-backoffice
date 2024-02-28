package bg.duosoft.ipas.core.model.patent;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CPatentDetails implements Serializable {
    private Integer drawings;
    private Integer drawingPublications;
    private Integer descriptionPages;
    private Integer inventionsGroup;
    private Integer claims;
    private List<CPatentAttachment> patentAttachments;
    private Date notInForceDate;
}
