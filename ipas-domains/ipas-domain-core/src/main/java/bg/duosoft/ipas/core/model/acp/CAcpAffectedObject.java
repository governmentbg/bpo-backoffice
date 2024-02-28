package bg.duosoft.ipas.core.model.acp;

import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CAcpAffectedObject implements Serializable {
    private static final long serialVersionUID = 2377500348430037427L;

    private Integer id;
    private CFileId fileId;
    private Integer registrationNbr;
    private String title;
    private CAcpExternalAffectedObject externalAffectedObject;
}
