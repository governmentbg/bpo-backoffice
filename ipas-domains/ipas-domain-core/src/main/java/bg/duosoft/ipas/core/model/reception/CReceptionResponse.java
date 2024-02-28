package bg.duosoft.ipas.core.model.reception;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * User: Georgi
 * Date: 4.6.2020 г.
 * Time: 15:06
 */
@Data
@NoArgsConstructor
public class CReceptionResponse implements Serializable {
    private Integer docflowDocumentId;
    private CDocumentId docId;
    private CDocumentSeqId docSeqId;
    private CFileId fileId;//only if a file is received
    private String externalSystemId;
    private List<CReceptionResponse> userdocReceptionResponses;

    public CReceptionResponse(CDocumentId docId, CDocumentSeqId docSeqId, CFileId fileId) {
        this.docId = docId;
        this.docSeqId = docSeqId;
        this.fileId = fileId;
    }
}
