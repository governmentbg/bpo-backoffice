package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.offidoc.COffidocId;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//TODO Object package and name
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CProcessInsertActionRequest implements Serializable {
    private CProcessId processId;
    private String actionType;
    private Date actionDate;
    private String notes1;
    private String notes2;
    private String notes3;
    private String notes4;
    private String notes5;
    private String notes;
    private Integer responsibleUser;
    private String specialFinalStatus;
    private Date manualDueDate;
    private Integer captureUser;
    private Integer certificateReference;
    private List<String> offidocTemplates;
    private Date recordalDate;
    private Date invalidationDate;
    private Boolean transferCorrespondenceAddress;
}