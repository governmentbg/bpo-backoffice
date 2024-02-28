package bg.duosoft.ipas.core.model.file;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.document.CDocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CFileRecordal implements Serializable {
    private CFileId fileId;
    private CDocumentId documentId;
    private String externalSystemId;
    private CActionId actionId;
    private Date date;
    private String type;
    private CDocumentId invalidationDocumentId;
    private Date invalidationDate;
    private String invalidationExternalSystemId;
    private CActionId invalidationActionId;
}


