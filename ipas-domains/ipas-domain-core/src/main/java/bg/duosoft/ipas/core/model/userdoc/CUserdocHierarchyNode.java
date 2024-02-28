package bg.duosoft.ipas.core.model.userdoc;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.document.CDocumentSeqId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 22.01.2021
 * Time: 14:35
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CUserdocHierarchyNode implements Serializable {
    private CProcessId processId;
    private CDocumentId documentId;
    private CDocumentSeqId documentSeqId;
    private CProcessId upperProcessId;
    private String userdocType;
    private String userdocTypeName;
    private List<CUserdocHierarchyNode> children;
    private String externalSystemId;
    private Date filingDate;
    private CFileId fileId;
    private String efilingUser;
    private String statusCode;
    private Integer responsibleUserId;
}
