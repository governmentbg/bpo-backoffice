package bg.duosoft.ipas.test.validator;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.patent.CPatent;
import bg.duosoft.ipas.core.service.patent.PatentService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.patent.PatentValidator;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.patent.IpPatent;
import bg.duosoft.ipas.test.TestBase;
import de.danielbechler.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: ggeorgiev
 * Date: 15.3.2019 г.
 * Time: 17:38
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) //poradi nqkakva prichina, ako ne se izchisti context-a sled kato se execute-ne klasa, posle gyrmqt testovete za Search-ovete!!!!
public class PatentValidatorTest extends TestBase {
    @Autowired
    private PatentService patentService;
    @Autowired
    private IpasValidatorCreator ipasValidatorCreator;
    @Autowired
    private IpoSearchService searchService;
    @Autowired
    private IndexService indexService;

    @Before
    public void setUp() {
        indexService.delete(IpPatent.class);
        indexService.index(new IpFilePK("BG", "Х", 1998, 472), IpPatent.class);
    }

    @Test
    @Transactional
    public void testValidateDuplicateDrawings() {
        CPatent patent = patentService.findPatent("BG", "У", 1998, 472001, true);
        patent.getTechnicalData().getDrawingList().stream().filter(f -> f.getDrawingNbr() != null && f.getDrawingNbr().longValue() == 121).forEach(d -> d.setDrawingNbr((long) 111));

        IpasValidator<CPatent> patentValidator = ipasValidatorCreator.create(false, PatentValidator.class);
        List<ValidationError> errors = patentValidator.validate(patent);
        if (!Collections.isEmpty(errors)) {
            errors.forEach(System.out::println);
        }
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("drawingData", errors.get(0).getPointer());
    }


}
