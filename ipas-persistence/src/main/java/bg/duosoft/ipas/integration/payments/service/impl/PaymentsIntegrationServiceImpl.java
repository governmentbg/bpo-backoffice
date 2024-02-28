package bg.duosoft.ipas.integration.payments.service.impl;

import bg.duosoft.ipas.core.model.payments.CLiabilityDetal;
import bg.duosoft.ipas.core.model.payments.CLiabilityRecord;
import bg.duosoft.ipas.core.model.payments.CPaymentXmlDetail;
import bg.duosoft.ipas.integration.payments.mapper.LiabilityDetailMapper;
import bg.duosoft.ipas.integration.payments.mapper.LiabilityRecordMapper;
import bg.duosoft.ipas.integration.payments.mapper.PaymentXmlDetailMapper;
import bg.duosoft.ipas.integration.payments.model.LiabilityCodeRecord;
import bg.duosoft.ipas.integration.payments.model.LiabilityRecord;
import bg.duosoft.ipas.integration.payments.model.PaymentLiabilityDetail;
import bg.duosoft.ipas.integration.payments.model.PaymentXmlDetail;
import bg.duosoft.ipas.integration.payments.service.PaymentsIntegrationService;
import bg.duosoft.ipas.properties.PropertyAccess;
import bg.duosoft.ipas.util.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentsIntegrationServiceImpl implements PaymentsIntegrationService {

    @Autowired
    private PropertyAccess propertyAccess;

    @Autowired
    private RestTemplate paymentsRestTemplate;

    @Autowired
    private LiabilityDetailMapper liabilityDetailMapper;

    @Autowired
    private LiabilityRecordMapper liabilityRecordMapper;

    @Autowired
    private PaymentXmlDetailMapper paymentXmlDetailMapper;

    @Override
    public CLiabilityRecord getLiabilityRecord(String referenceNumber) {
        try {
            ResponseEntity<LiabilityRecord> result = paymentsRestTemplate.getForEntity(concatToBaseRestServiceUrl(SELECT_LIABILITY_URL), LiabilityRecord.class, referenceNumber);
            LiabilityRecord liabilityRecord = result.getBody();
            if (Objects.isNull(liabilityRecord))
                return null;

            return liabilityRecordMapper.toCore(liabilityRecord);
        } catch (RestClientException e) {
            log.error("Error reading payment data...", e);
            throw e;
        }
    }

    @Override
    public List<CLiabilityDetal> getAllLiabilityDetails(String referenceNumber) {
        if (StringUtils.isEmpty(referenceNumber))
            return null;

        try {
            ResponseEntity<PaymentLiabilityDetail[]> result = paymentsRestTemplate.getForEntity(concatToBaseRestServiceUrl(SELECT_LIABILITY_LIST_URL), PaymentLiabilityDetail[].class, referenceNumber);
            List<PaymentLiabilityDetail> paymentLiabilityDetails = Objects.requireNonNull(result.getBody()).length == 0 ? null : Arrays.asList(result.getBody());
            if (CollectionUtils.isEmpty(paymentLiabilityDetails))
                return null;

            return liabilityDetailMapper.toCoreList(paymentLiabilityDetails);
        } catch (RestClientException e) {
            log.error("Error reading payment data...", e);
            throw e;
        }
    }

    @Override
    @Cacheable(value = "liabilityCodeNamesMap", unless="#result == null")
    public Map<String, String> getLiabilityCodeNamesMap() {
        try {
            ResponseEntity<LiabilityCodeRecord[]> result = paymentsRestTemplate.getForEntity(concatToBaseRestServiceUrl(LIABILITY_CODE_NOMENCLATURE_URL), LiabilityCodeRecord[].class);
            List<LiabilityCodeRecord> codes = Objects.requireNonNull(result.getBody()).length == 0 ? null : Arrays.asList(result.getBody());
            if (CollectionUtils.isEmpty(codes))
                return null;

            return codes.stream()
                    .collect(Collectors.toMap(LiabilityCodeRecord::getLiabilityCode, LiabilityCodeRecord::getDescription));
        } catch (RestClientException e) {
            log.error("Error reading liability code nomenclature data...", e);
            return null;
        }
    }

    @Override
    public boolean containsNotLinkedPayments(String referenceNumber) {
        if (!StringUtils.isEmpty(referenceNumber)) {
            List<PaymentXmlDetail> paymentXmlDetails = getPaymentXmlDetails(referenceNumber);
            if (CollectionUtils.isEmpty(paymentXmlDetails))
                return false;

            PaymentXmlDetail detailHavingAmountOutstanding = paymentXmlDetails.stream()
                    .filter(paymentXmlDetail -> paymentXmlDetail.getAmountOutstanding().compareTo(BigDecimal.ZERO) != 0)
                    .findAny()
                    .orElse(null);

            return Objects.nonNull(detailHavingAmountOutstanding);
        }
        return false;
    }

    //zasega nqma nujda da e public, syotvetno ne sym pravil core obekt za PaymentXmlDetails!
    private List<PaymentXmlDetail> getPaymentXmlDetails(String referenceNumber) {
        try {
            ResponseEntity<PaymentXmlDetail[]> result = paymentsRestTemplate.getForEntity(concatToBaseRestServiceUrl(PAYMENT_XML_DETAILS), PaymentXmlDetail[].class, referenceNumber);
            return result.getBody().length == 0 ? null : Arrays.asList(result.getBody());
        } catch (RestClientException e) {
            log.error("Error reading payment data...", e);
            throw new RuntimeException(e);
        }
    }
    private String concatToBaseRestServiceUrl(String service) {
        return propertyAccess.getPaymentRestServiceUrl() + service;
    }

    @Cacheable(value = "notLinkedPayments")
    public List<CPaymentXmlDetail> getNotLinkedPayments(Date dateFrom, Date dateTo, Integer page, Integer rows) {
        ResponseEntity<PaymentXmlDetail[]> result = paymentsRestTemplate.getForEntity(concatToBaseRestServiceUrl(NOT_LINKED_PAYMENTS), PaymentXmlDetail[].class, DateUtils.formatDate(dateFrom), DateUtils.formatDate(dateTo), page, rows);
        List<PaymentXmlDetail> res = (result.getBody().length == 0) ? new ArrayList<>() : Arrays.asList(result.getBody());
        return paymentXmlDetailMapper.toCoreList(res);
    }

}
