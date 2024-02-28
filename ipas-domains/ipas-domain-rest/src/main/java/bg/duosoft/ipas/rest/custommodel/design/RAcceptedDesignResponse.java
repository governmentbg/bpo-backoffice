package bg.duosoft.ipas.rest.custommodel.design;

import bg.duosoft.ipas.rest.model.file.RFileId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RAcceptedDesignResponse {
    private RFileId fileId;
}
