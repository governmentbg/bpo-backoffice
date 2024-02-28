package bg.duosoft.ipas.core.model.process;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.core.model.file.CProcessId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CTopProcessFileData implements Serializable {
    private CProcessId processId;
    private CFileId fileId;
    private String title;
    private String statusCode;
    private String statusName;
    private Integer regNumber;
}
