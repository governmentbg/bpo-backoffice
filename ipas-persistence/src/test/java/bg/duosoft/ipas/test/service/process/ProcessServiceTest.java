package bg.duosoft.ipas.test.service.process;

import bg.duosoft.ipas.core.model.file.CProcessId;
import bg.duosoft.ipas.core.model.process.CProcessParentData;
import bg.duosoft.ipas.core.service.process.ProcessService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: ggeorgiev
 * Date: 20.01.2021
 * Time: 15:16
 */
public class ProcessServiceTest extends TestBase {
    @Autowired
    private ProcessService processService;
    @Test
    public void testReadProcessHierarchy() {
        CProcessParentData hierarchy = processService.generateProcessHierarchy(new CProcessId("2", 121371));
        System.out.println(hierarchy);
    }
}
