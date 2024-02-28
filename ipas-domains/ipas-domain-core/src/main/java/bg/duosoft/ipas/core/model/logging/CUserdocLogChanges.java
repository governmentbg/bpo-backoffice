package bg.duosoft.ipas.core.model.logging;

import bg.duosoft.ipas.core.model.document.CDocumentId;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.Map;

/**
 * User: ggeorgiev
 * Date: 03.12.2021
 * Time: 14:02
 */
@Data
public class CUserdocLogChanges extends CLogChangesBase{
    private CDocumentId documentId;

}
