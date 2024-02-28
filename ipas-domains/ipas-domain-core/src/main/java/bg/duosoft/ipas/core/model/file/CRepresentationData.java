package bg.duosoft.ipas.core.model.file;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.person.CRepresentative;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CRepresentationData
        implements Serializable {
    private static final long serialVersionUID = -7288694054850215527L;
    private List<CRepresentative> representativeList;
    private CDocumentId documentId_PowerOfAttorneyRegister;
}


