package bg.duosoft.ipas.test.diff;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CNiceClass;
import bg.duosoft.ipas.core.service.impl.logging.diff.DiffGenerator;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.GregorianCalendar;

/**
 * User: ggeorgiev
 * Date: 29.1.2019 г.
 * Time: 16:35
 */

public class MarkChangesLogTest extends TestBase {
    @Autowired
    private MarkService markService;


    @Test
    @Transactional

    public void testGenerateLogChange() {

        CMark original = markService.findMark("BG", "N", 2013, 130109, true);
        CMark changed = markService.findMark("BG", "N", 2013, 130109, true);
        changed.getFile().getFilingData().setFilingDate(new GregorianCalendar(2020, 0, 1).getTime());
        changed.getFile().getFilingData().setLawCode(4);
        CNiceClass niceClass = new CNiceClass();
        niceClass.setNiceClassNbr(1);
        niceClass.setNiceClassEdition(1);
        niceClass.setNiceClassGlobalStatus("R");
        niceClass.setNiceClassDetailedStatus("R");
        niceClass.setNiceClassDescription("консултантски услуги, свързани с инсталиране на компютри");
        niceClass.setNiceClassVersion("1");
        changed.getProtectionData().getNiceClassList().add(niceClass);

        niceClass = new CNiceClass();
        niceClass.setNiceClassNbr(12);
        niceClass.setNiceClassEdition(12);
        niceClass.setNiceClassGlobalStatus("R2");
        niceClass.setNiceClassDetailedStatus("R2");
        niceClass.setNiceClassDescription("консултантски услуги, свързани с инсталиране на компютри2");
        niceClass.setNiceClassVersion("12");
        changed.getProtectionData().getNiceClassList().add(niceClass);


        String result = DiffGenerator.create(original, changed).getResult();
        System.out.println(result);


    }


    @Test
    @Transactional
    public void testGenerateLogChange2() {

        CMark original = markService.findMark("BG", "N", 2013, 130120, true);
        CMark changed = markService.findMark("BG", "N", 2013, 130120, true);
        changed.getFile().getFilingData().setFilingDate(new GregorianCalendar(2020, 0, 1).getTime());
        changed.getFile().getRegistrationData().setEntitlementDate(new GregorianCalendar(2020,0,1).getTime());


        String result = DiffGenerator.create(original, changed).getResult();
        System.out.println(result);



    }
}
