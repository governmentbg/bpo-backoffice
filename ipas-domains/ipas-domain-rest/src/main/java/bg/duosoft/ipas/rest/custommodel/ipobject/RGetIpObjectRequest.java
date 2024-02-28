package bg.duosoft.ipas.rest.custommodel.ipobject;

import bg.duosoft.ipas.rest.model.file.RFileId;
import lombok.*;

/**
 * User: Georgi
 * Date: 14.7.2020 г.
 * Time: 13:35
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RGetIpObjectRequest {
    private RFileId fileId;
    private boolean addAttachments;
}
