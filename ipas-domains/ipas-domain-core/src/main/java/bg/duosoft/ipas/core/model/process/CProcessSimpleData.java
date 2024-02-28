package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.person.CUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 21.6.2019 г.
 * Time: 12:17
 */
@Data
public class CProcessSimpleData implements Serializable {
    private CProcessId processId;
    private Date creationDate;
    private Date statusDate;
    private String statusCode;
    private CUser responsibleUser;
    private CProcessId fileProcessId;
    private CDocumentId documentId;
    private CFileId fileId;
}
