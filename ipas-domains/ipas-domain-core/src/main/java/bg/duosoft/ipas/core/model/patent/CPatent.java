package bg.duosoft.ipas.core.model.patent;

import bg.duosoft.ipas.core.model.design.CProductTerm;
import bg.duosoft.ipas.core.model.efiling.CEFilingData;
import bg.duosoft.ipas.core.model.file.CFile;
import bg.duosoft.ipas.core.model.file.CRelationshipExtended;
import bg.duosoft.ipas.core.model.plant.CPlant;
import bg.duosoft.ipas.core.model.spc.CSpcExtended;
import de.danielbechler.diff.inclusion.Inclusion;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CPatent
        implements Serializable
{
    private static final long serialVersionUID = -4985662158486518798L;
    private Boolean patentContainsDrawingList;
    private Boolean indReadDrawingList;
    private Integer rowVersion;
    private CPctApplicationData pctApplicationData;
    private CTechnicalData technicalData;
    private boolean reception;
    @Valid
    private CFile file;
    private CAuthorshipData authorshipData;
    private CPlant plantData;
    private CSpcExtended spcExtended;
    @Valid
    private CRelationshipExtended relationshipExtended;
    private CPatentDetails patentDetails;
    private CProductTerm productTerm;
    private CEFilingData patentEFilingData;

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public Boolean getIndReadDrawingList() {
        return indReadDrawingList;
    }

    @ObjectDiffProperty(inclusion = Inclusion.EXCLUDED)
    public CProductTerm getProductTerm() {
        return productTerm;
    }
}