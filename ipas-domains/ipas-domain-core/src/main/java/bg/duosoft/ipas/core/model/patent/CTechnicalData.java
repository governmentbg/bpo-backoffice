package bg.duosoft.ipas.core.model.patent;


import bg.duosoft.ipas.core.model.design.CPatentLocarnoClasses;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CTechnicalData implements Serializable {

    private static final long serialVersionUID = -167724700361691338L;
    private String title;
    private String englishTitle;
    private byte[] wordfileTitle;
    private String mainAbstract;
    private String englishAbstract;
    private String lastClaimsPageRef;
    private String lastDescriptionPageRef;
    private Date noveltyDate;
    private Boolean hasIpc;
    private Boolean hasCpc;
    private Boolean hasCpcClasses;
    private List<CPatentLocarnoClasses> locarnoClassList;
    private List<CPatentIpcClass> ipcClassList;
    private List<CPatentCpcClass> cpcClassList;
    private List<CClaim> claimList;
    private List<CDrawing> drawingList;
    private List<CPatentCitation> citationList;


    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public String getBase64EncodedWordFileTitle() {
        if (Objects.isNull(this.wordfileTitle))
            return null;
        return Base64.getEncoder().encodeToString(this.wordfileTitle);
    }
}
