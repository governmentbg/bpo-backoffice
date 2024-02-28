package bg.duosoft.ipas.core.model.acp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAcpCheckReason implements Serializable {
    private CAcpCheckReasonNomenclature acpCheckReason;
}
