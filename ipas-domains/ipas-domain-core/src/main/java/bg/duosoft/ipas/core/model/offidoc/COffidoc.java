package bg.duosoft.ipas.core.model.offidoc;

import bg.duosoft.ipas.core.model.action.CActionId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.model.process.CProcessSimpleData;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class COffidoc implements Serializable {
    private static final long serialVersionUID = 7473796954304113177L;
    private COffidocId offidocId;
    private CProcessId processId;
    private CProcessParentData offidocParentData;
    private Date printDate;
    private String externalSystemId;
    private Integer abdocsDocumentId;
    private COffidocType offidocType;
    private CActionId actionId;
    private CProcessSimpleData processSimpleData;
    private Boolean hasChildren;
    private COffidocPublishedDecision publishedDecision;
}