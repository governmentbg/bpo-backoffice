package bg.duosoft.ipas.test.service.action;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.action.CActionType;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.service.nomenclature.ActionTypeService;
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

public class ActionTypeServiceTest extends TestBase {
    @Autowired
    private ActionTypeService actionTypeService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Test
    @Transactional
    public void findActionByProcTypesAndActionName() {

        List<String> procTypes = userdocTypesService.getAllProcTypes();
        List<CActionType> all = actionTypeService
                .findActionTypesByProcTypesAndActionName(procTypes, "тм");

        assertEquals(223, all.size());
    }
}
