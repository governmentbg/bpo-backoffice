package bg.duosoft.ipas.core.model.reception;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CReceptionType implements Serializable {
    private Integer id;
    private String title;
    private String shortTitle;
    private String titleEn;
    private String shortTitleEn;
    private String fileType;
    private String applTyp;
    private Boolean receptionOnCounter;
    private Boolean receptionFromExistingDocument;
    private List<CReceptionTypeAdditionalUserdoc> additionalUserdocs;
}
