package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import de.danielbechler.diff.introspection.ObjectDiffProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CUserdocExtraData implements Serializable {
    private CDocumentId documentId;
    private CUserdocExtraDataType type;
    private String textValue;
    private Integer numberValue;
    private Date dateValue;
    private Boolean booleanValue;

    @ObjectDiffProperty(equalsOnlyValueProviderMethod = "getCode")
    public CUserdocExtraDataType getType() {
        return type;
    }
}


