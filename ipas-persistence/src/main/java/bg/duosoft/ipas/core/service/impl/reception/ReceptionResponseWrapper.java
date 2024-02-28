package bg.duosoft.ipas.core.service.impl.reception;

import bg.duosoft.ipas.core.model.reception.CReceptionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User: ggeorgiev
 * Date: 12.07.2022
 * Time: 11:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionResponseWrapper {
    private CReceptionResponse receptionResponse;
}
