package bg.duosoft.ipas.test.service.nomenclature;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.model.userdoc.CUserdocType;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserdocTypeServiceTest extends TestBase {
    @Autowired
    private UserdocTypesService userdocTypesService;

    @Test
    @Transactional
    public void getAllProcTypes() {
        List<String> all = userdocTypesService.getAllProcTypes();

        assertEquals(11, all.size());
    }
    @Test
    @Transactional
    public void getUserdocType() {
        CUserdocType udt = userdocTypesService.selectUserdocTypeById("ЕПИВ");
        assertNotNull(udt);
        assertEquals("ЕПИВ", udt.getUserdocType());
    }
}
