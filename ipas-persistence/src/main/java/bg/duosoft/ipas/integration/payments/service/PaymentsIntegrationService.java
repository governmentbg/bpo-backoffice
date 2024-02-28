package bg.duosoft.ipas.integration.payments.service;

import bg.duosoft.ipas.core.model.payments.CLiabilityDetal;
import bg.duosoft.ipas.core.model.payments.CLiabilityRecord;
import bg.duosoft.ipas.core.model.payments.CPaymentXmlDetail;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PaymentsIntegrationService {

    String SELECT_LIABILITY_URL = "/liability?referenceNumber={referenceNumber}";
    String INSERT_LIABILITY_URL = "/liabilitydetail/insert";
    String SELECT_LIABILITY_LIST_URL = "/liabilitydetails?referenceNumber={referenceNumber}&includeChildren=true";
    String DESIGN_LIABLITY_CODES_URL = "/design/liabilityCodes";
    String LIABILITY_CODE_NOMENCLATURE_URL = "/liabilityCodes";
    String PAYMENT_XML_DETAILS = "/paymentxmldetails?referenceNumber={referenceNumber}";
    String NOT_LINKED_PAYMENTS = "/notlinkedpayments?dateFrom={dateFrom}&dateTo={dateTo}&page={page}&rows={rows}";

    CLiabilityRecord getLiabilityRecord(String referenceNumber);

    List<CLiabilityDetal> getAllLiabilityDetails(String referenceNumber);

    Map<String,String> getLiabilityCodeNamesMap();

    public boolean containsNotLinkedPayments(String referenceNumber);

    public List<CPaymentXmlDetail> getNotLinkedPayments(Date dateFrom, Date dateTo, Integer page, Integer rows);

}
