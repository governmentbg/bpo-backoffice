package bg.duosoft.ipas.test.validator;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.search.IndexService;
import bg.duosoft.ipas.core.service.search.IpoSearchService;
import bg.duosoft.ipas.core.validation.config.IpasValidationException;
import bg.duosoft.ipas.core.validation.config.IpasValidator;
import bg.duosoft.ipas.core.validation.config.IpasValidatorCreator;
import bg.duosoft.ipas.core.validation.config.ValidationError;
import bg.duosoft.ipas.core.validation.mark.MarkValidateApplicationTypeSubTypeAndLaw;
import bg.duosoft.ipas.persistence.model.entity.file.IpFilePK;
import bg.duosoft.ipas.persistence.model.entity.mark.IpMark;
import bg.duosoft.ipas.test.TestBase;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) //poradi nqkakva prichina, ako ne se izchisti context-a sled kato se execute-ne klasa, posle gyrmqt testovete za Search-ovete!!!!
public class MarkValidationTest extends TestBase {
    @Autowired
    private MarkService markService;
    @Autowired
    private IpasValidatorCreator ipasValidatorCreator;

    @Autowired
    private IpoSearchService searchService;

    @Autowired
    private IndexService indexService;

    @Before
    public void setUp() {
        indexService.delete(IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2008, 91873), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2012, 102812), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2012, 102813), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2012, 114503), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2013, 27791), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2013, 91540), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2013, 91541), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2013, 91542), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2013, 107818), IpMark.class);
        indexService.index(new IpFilePK("BG", "D", 2014, 91467), IpMark.class);
        indexService.index(new IpFilePK("BG", "N", 2012, 122197), IpMark.class);
        indexService.index(new IpFilePK("BG", "N", 2015, 135790), IpMark.class);
    }
    @Test
    @Transactional
//    @Ignore
    public void updateMark() {
        CMark mark = markService.findMark("BG", "N", 2007, 99998, true);
        mark.getFile().getFilingData().setLawCode(3);
        mark.getFile().getFilingData().setFilingDate(null);
        try {
            markService.updateMark(mark);
        } catch (IpasValidationException e) {
            e.getErrors().forEach(System.out::println);
            assertNotNull(e.getErrors());
            assertEquals(2, e.getErrors().size());
        }
    }

    @Test
    @Transactional
//    @Ignore
    public void validateMark() {
        CMark mark = markService.findMark("BG", "N", 2007, 99998, true);
        mark.getFile().getFilingData().setLawCode(3);
        mark.getFile().getFilingData().setFilingDate(null);
        IpasValidator<CMark> validator = ipasValidatorCreator.create(true, MarkValidateApplicationTypeSubTypeAndLaw.class);
        List<ValidationError> errors = validator.validate(mark);
        errors.forEach(System.out::println);
        assertNotNull(errors);
        assertEquals(2, errors.size());

    }
}
