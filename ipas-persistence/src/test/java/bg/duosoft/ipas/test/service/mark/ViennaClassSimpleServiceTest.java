package bg.duosoft.ipas.test.service.mark;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.mark.CViennaClassSimple;
import bg.duosoft.ipas.core.service.nomenclature.ViennaClassSimpleService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;


@Transactional
public class ViennaClassSimpleServiceTest extends TestBase {
    @Autowired
    private ViennaClassSimpleService viennaClassSimpleService;

    @Test
    public void testGetAllViennaClasses() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode(null);
        assertEquals(2293, viennaClassesSimple.size());
    }

    @Test
    public void testGetAllViennaClassesMaxResult_10() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode(10);
        assertEquals(10, viennaClassesSimple.size());
    }

    @Test
    public void testGetViennaClassesByCode_01() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode("01", null);
        assertEquals(134, viennaClassesSimple.size());
    }

    @Test
    public void testGetViennaClassesByCode_01_MaxResult10() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode("01", 10);
        assertEquals(10, viennaClassesSimple.size());
    }

    @Test
    public void testGetViennaClassesByCode_01_dot() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode("01.", null);
        assertEquals(133, viennaClassesSimple.size());
    }

    @Test
    public void testGetViennaClassesByCode_01_dot_01() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode("01.01", null);
        assertEquals(20, viennaClassesSimple.size());
    }

    @Test
    public void testGetViennaClassesByCode_01_dot_01_dot_1() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode("01.01.1", null);
        assertEquals(7, viennaClassesSimple.size());
    }

    @Test
    public void testGetViennaClassesByCode_01_dot_01_dot_1_dot_expectZero() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode("01.01.1.", null);
        assertEquals(0, viennaClassesSimple.size());
    }

    @Test
    public void testGetViennaClassesByCode_01_dot_01_dot_10() {
        List<CViennaClassSimple> viennaClassesSimple = viennaClassSimpleService.findAllByViennaCode("01.01.10", null);

        assertEquals(1, viennaClassesSimple.size());

        CViennaClassSimple viennaClassSimple = viennaClassesSimple.get(0);
        assertEquals("01.01.10", viennaClassSimple.getViennaClass());
        assertEquals("*Stars with more than four points", viennaClassSimple.getViennaDescription());
    }
}
