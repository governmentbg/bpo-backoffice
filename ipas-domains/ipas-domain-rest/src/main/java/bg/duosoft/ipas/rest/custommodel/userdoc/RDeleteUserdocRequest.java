package bg.duosoft.ipas.rest.custommodel.userdoc;

import bg.duosoft.ipas.rest.model.document.RDocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: Georgi
 * Date: 5.10.2020 г.
 * Time: 15:39
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RDeleteUserdocRequest {
    private RDocumentId documentId;
    private boolean deleteInDocflowSystem;
}
