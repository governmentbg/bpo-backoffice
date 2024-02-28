package bg.duosoft.ipas.core.service.patent;

import bg.duosoft.ipas.persistence.model.nonentity.PatentCertificatePaidFeesResult;

import java.util.Date;
import java.util.List;

public interface PatentCertificatePaidFeesService {

    List<PatentCertificatePaidFeesResult> selectPaidFees(Date paymentDateFrom, Date paymentDateTo, String payer);

}
