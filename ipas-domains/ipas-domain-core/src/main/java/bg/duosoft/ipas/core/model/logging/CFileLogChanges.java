package bg.duosoft.ipas.core.model.logging;

import bg.duosoft.ipas.core.model.file.CFileId;
import lombok.Data;

/**
 * User: ggeorgiev
 * Date: 03.12.2021
 * Time: 14:02
 */
@Data
public class CFileLogChanges extends CLogChangesBase {
    private CFileId fileId;
}
