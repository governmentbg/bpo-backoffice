package bg.duosoft.ipas.core.service.reception;

import bg.duosoft.ipas.core.model.reception.CReception;
import bg.duosoft.ipas.core.model.reception.CReceptionResponse;

/**
 * User: ggeorgiev
 * Date: 11.08.2021
 * Time: 14:22
 *
 * This interface should be autowired only in the service layer!!!!
 * The ReceptionService's createReception method might be used in the other layers - UI / rest
 */
public interface InternalReceptionService {
    public CReceptionResponse createReception(CReception receptionForm);
}
