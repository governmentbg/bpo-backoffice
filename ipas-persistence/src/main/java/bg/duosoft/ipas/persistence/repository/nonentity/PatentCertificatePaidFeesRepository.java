package bg.duosoft.ipas.persistence.repository.nonentity;

import bg.duosoft.ipas.persistence.model.nonentity.PatentCertificatePaidFeesResult;

import java.util.Date;
import java.util.List;

public interface PatentCertificatePaidFeesRepository {

    List<PatentCertificatePaidFeesResult> selectPaidFees(Date paymentDateFrom, Date paymentDateTo, String payer);

}
