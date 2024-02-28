package bg.duosoft.ipas.test.repository.ext;

import bg.duosoft.ipas.persistence.model.nonentity.ExtLiabilityDetailsExtended;
import bg.duosoft.ipas.persistence.repository.nonentity.ExtLiabilityDetailsExtendedRepository;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.date.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: ggeorgiev
 * Date: 13.01.2021
 * Time: 16:20
 */
public class ExtLiabilityDetailsRepositoryExtendedTest extends TestBase {
    @Autowired
    private ExtLiabilityDetailsExtendedRepository extLiabilityDetailsExtendedRepository;
    @Test
    @Transactional
    public void testGetLastPayments() {
        List<ExtLiabilityDetailsExtended> res = extLiabilityDetailsExtendedRepository.getLastPayments(DateUtils.convertToDate(LocalDate.of(2015, 1, 1)), null, Arrays.asList(1), 0, null, 1000, null, null);
        assertEquals(1000, res.size());
    }

    @Test
    @Transactional
    public void testGetLastPaymentsCount() {
        long res = extLiabilityDetailsExtendedRepository.getLastPaymentsCount(DateUtils.convertToDate(LocalDate.of(2015, 1, 1)), null, Arrays.asList(1), 0);
        assertEquals(78933, res);
    }
}
