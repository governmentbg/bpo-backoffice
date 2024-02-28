package bg.duosoft.ipas.core.service.impl.patent;

import bg.duosoft.ipas.core.service.patent.PatentCertificatePaidFeesService;
import bg.duosoft.ipas.persistence.model.nonentity.PatentCertificatePaidFeesResult;
import bg.duosoft.ipas.persistence.repository.nonentity.PatentCertificatePaidFeesRepository;
import bg.duosoft.logging.annotation.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
@LogExecutionTime
@RequiredArgsConstructor
public class PatentCertificatePaidFeesServiceImpl implements PatentCertificatePaidFeesService {

    private final PatentCertificatePaidFeesRepository patentCertificatePaidFeesRepository;

    @Override
    public List<PatentCertificatePaidFeesResult> selectPaidFees(Date paymentDateFrom, Date paymentDateTo, String payer) {
        return patentCertificatePaidFeesRepository.selectPaidFees(paymentDateFrom, paymentDateTo, payer);
    }
}
