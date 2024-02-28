package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProcessFileData {
    private CProcessId processId;
    private CFileId fileId;
    private String title;
    private String statusCode;
    private String statusName;
    private Integer regNumber;
}
