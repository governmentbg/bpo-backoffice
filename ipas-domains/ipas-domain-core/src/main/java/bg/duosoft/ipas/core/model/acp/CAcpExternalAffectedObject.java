package bg.duosoft.ipas.core.model.acp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CAcpExternalAffectedObject implements Serializable {
    private static final long serialVersionUID = -2593469817312896246L;

    private Integer id;
    private String externalId;
    private String registrationNbr;
    private String name;
}
