package bg.duosoft.ipas.core.service.impl.reception.ebd;

import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public
class ReceiveEuropeanPatentResult {
    private CReceptionResponse insertPatentReceptionResponse;
    private CReceptionResponse insertFinalUserdocResponse;
}
