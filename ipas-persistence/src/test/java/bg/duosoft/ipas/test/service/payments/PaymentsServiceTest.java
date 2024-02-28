package bg.duosoft.ipas.test.service.payments;

import bg.duosoft.ipas.core.model.CExtLiabilityDetail;
import bg.duosoft.ipas.core.service.payments.PaymentsService;
import bg.duosoft.ipas.integration.payments.service.PaymentsIntegrationService;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 19.08.2021
 * Time: 11:14
 */
public class PaymentsServiceTest extends TestBase {
    @Autowired
    private PaymentsIntegrationService paymentsIntegrationService;

    @Autowired
    private PaymentsService paymentsService;

    @Test
    @Transactional
    public void testReadLiability() {
        CExtLiabilityDetail liability = paymentsService.getLiabilityDetailById(483523);
        Assert.assertNotNull(liability);
        assertEquals("BG", liability.getFileId().getFileSeq());
        assertEquals("P", liability.getFileId().getFileType());
        assertEquals((Object)1979, liability.getFileId().getFileSeries());
        assertEquals((Object)44522, liability.getFileId().getFileNbr());
        assertEquals("P12010", liability.getLiabilityCode());
        assertEquals("НЕИЗВЕСТЕН", liability.getLastPayerName());
        assertEquals("UNKN", liability.getLastPaymentType());
        assertEquals((Object)10, liability.getAnnuityNumber());
        assertEquals(LocalDate.of(2014, 11, 26), DateUtils.convertToLocalDate(liability.getDateCreated()));
        assertEquals(LocalDate.of(1989, 8, 1), DateUtils.convertToLocalDate(liability.getExpirationDate()));
        assertEquals(LocalDate.of(1988, 10, 20), DateUtils.convertToLocalDate(liability.getLastDatePayment()));
        assertEquals(false, liability.isProcessed());
        assertEquals(new BigDecimal("165.00"), liability.getAmount());
        assertEquals(new BigDecimal("0.00"), liability.getAmountOutstanding());


    }
    @Test
    @Ignore
    public void testGetNotLinkedPayments() {
        paymentsIntegrationService.getNotLinkedPayments(DateUtils.convertToDate(LocalDate.of(2021, 1, 1)), null, 1, 10).forEach(System.out::println);
    }

    @Test
    @Transactional
    @Ignore
    public void testGetNotLinkedPaymentsPerResponsibleUser() {
        paymentsService.getNotLinkedPaymentsPerResponsibleUsers(DateUtils.convertToDate(LocalDate.of(2021, 1, 1)), null, Arrays.asList(1)).forEach(System.out::println);
    }

}
