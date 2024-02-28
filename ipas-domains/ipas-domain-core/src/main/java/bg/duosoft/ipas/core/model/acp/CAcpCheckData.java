package bg.duosoft.ipas.core.model.acp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CAcpCheckData implements Serializable  {
    private Date checkDate;
    private CAcpCheckResult acpCheckResult;
}
