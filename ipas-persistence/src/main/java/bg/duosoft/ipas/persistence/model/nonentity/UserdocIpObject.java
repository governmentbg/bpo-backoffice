package bg.duosoft.ipas.persistence.model.nonentity;

import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserdocIpObject {
    private String registrationNbr;
    private String title;
    private CFileId cFileId;
    private String responsibleUserName;
    private String objectStatusName;
}
