package bg.duosoft.ipas.persistence.repository.nonentity.impl;

import bg.duosoft.ipas.core.model.file.CFileId;
import bg.duosoft.ipas.integration.payments.service.PaymentsIntegrationService;
import bg.duosoft.ipas.persistence.model.nonentity.PatentCertificatePaidFeesResult;
import bg.duosoft.ipas.persistence.repository.BaseRepositoryCustomImpl;
import bg.duosoft.ipas.persistence.repository.nonentity.PatentCertificatePaidFeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PatentCertificatePaidFeesRepositoryImpl extends BaseRepositoryCustomImpl implements PatentCertificatePaidFeesRepository {

    private final PaymentsIntegrationService paymentsIntegrationService;

    @Override
    public List<PatentCertificatePaidFeesResult> selectPaidFees(Date paymentDateFrom, Date paymentDateTo, String payer) {
        Query query = em.createNativeQuery("SELECT l.FILE_SEQ, l.FILE_TYP, l.FILE_SER, l.FILE_NBR,l.liability_code,l.last_date_payment,l.amount,l.LAST_PAYER_NAME,l.id,s.STATUS_NAME,f.REGISTRATION_NBR " +
                " FROM IPASPROD.ext_liability_details l " +
                " JOIN IPASPROD.IP_FILE f on f.FILE_SEQ = l.FILE_SEQ AND f.FILE_TYP = l.FILE_TYP AND f.FILE_SER = l.FILE_SER AND f.FILE_NBR = l.FILE_NBR " +
                " JOIN IPASPROD.IP_PROC p on p.FILE_SEQ = l.FILE_SEQ AND p.FILE_TYP = l.FILE_TYP AND p.FILE_SER = l.FILE_SER AND p.FILE_NBR = l.FILE_NBR " +
                " JOIN IPASPROD.CF_STATUS s on p.PROC_TYP = s.PROC_TYP and p.STATUS_CODE = s.STATUS_CODE " +
                " WHERE l.FILE_TYP IN ('T', 'P', 'S') " +
                "  AND l.last_date_payment IS NOT NULL " +
                "  AND l.annuity_number IS NOT NULL " +
                "  AND l.liability_code <> 'P12100' " +
                "  AND l.LAST_PAYER_NAME LIKE :payer " +
                "  AND l.last_date_payment >= :paymentDateFrom " +
                "  AND l.last_date_payment <= :paymentDateTo ORDER BY l.last_date_payment");

        query.setParameter("paymentDateFrom", paymentDateFrom);
        query.setParameter("paymentDateTo", paymentDateTo);
        query.setParameter("payer", "%" + payer + "%");

        List<Object[]> resultList = query.getResultList();
        if (CollectionUtils.isEmpty(resultList))
            return null;

        return convertResult(resultList);
    }

    private List<PatentCertificatePaidFeesResult> convertResult(List<Object[]> resultList) {
        Map<String, String> liabilityCodeNamesMap = paymentsIntegrationService.getLiabilityCodeNamesMap();

        List<PatentCertificatePaidFeesResult> result = new ArrayList<>();
        for (Object[] object : resultList) {
            result.add(
                    PatentCertificatePaidFeesResult.builder()
                            .fileId(new CFileId((String) object[0], (String) object[1], ((Number) object[2]).intValue(), ((Number) object[3]).intValue()))
                            .liabilityCode((String) object[4])
                            .liabilityCodeName(liabilityCodeNamesMap.get((String) object[4]))
                            .paymentDate((Date) object[5])
                            .amount((BigDecimal) object[6])
                            .payer((String) object[7])
                            .id(((Number) object[8]).intValue())
                            .statusName((String) object[9])
                            .regNumber(Objects.isNull(object[10]) ? null : ((Number) object[10]).intValue())
                            .build()
            );

        }
        return result;
    }

}
