package bg.duosoft.ipas.rest.custommodel.userdoc;

import bg.duosoft.ipas.rest.model.file.RFileId;
import bg.duosoft.ipas.rest.model.reception.RReceptionResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RAcceptedUserdocResponse {
    private RReceptionResponse receptionResponse;
    private RFileId affectedFileId;

    public RAcceptedUserdocResponse(RReceptionResponse receptionResponse) {
        this.receptionResponse = receptionResponse;
    }
}
