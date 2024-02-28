package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.ExtLiabilityDetailsExtended;

import java.util.Date;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 18.08.2021
 * Time: 10:59
 */
public interface ExtLiabilityDetailsExtendedRepository {

    List<ExtLiabilityDetailsExtended> getLastPayments(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed, Integer start, Integer limit, String sortColumn, String sortOrder);

    long getLastPaymentsCount(Date dateLastPaymentFrom, Date dateLastPaymentTo, List<Integer> responsibleUsers, Integer processed);
}
