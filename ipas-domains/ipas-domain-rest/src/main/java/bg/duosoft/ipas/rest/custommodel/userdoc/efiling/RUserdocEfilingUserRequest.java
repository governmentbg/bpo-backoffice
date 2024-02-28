package bg.duosoft.ipas.rest.custommodel.userdoc.efiling;

import bg.duosoft.ipas.rest.model.document.RDocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RUserdocEfilingUserRequest {
    private String externalSystemId;
    private String user;
}
