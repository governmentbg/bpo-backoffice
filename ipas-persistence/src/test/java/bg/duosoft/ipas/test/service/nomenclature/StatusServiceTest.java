package bg.duosoft.ipas.test.service.nomenclature;

import bg.duosoft.ipas.config.IpasDatabaseConfig;
import bg.duosoft.ipas.core.model.mark.CMark;
import bg.duosoft.ipas.core.model.mark.CMarkAttachment;
import bg.duosoft.ipas.core.model.miscellaneous.CStatus;
import bg.duosoft.ipas.core.service.mark.MarkService;
import bg.duosoft.ipas.core.service.nomenclature.StatusService;
import bg.duosoft.ipas.core.service.nomenclature.UserdocTypesService;
import bg.duosoft.ipas.test.TestBase;
import bg.duosoft.ipas.util.mark.MarkSignDataAttachmentUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StatusServiceTest extends TestBase {
    @Autowired
    private StatusService statusService;

    @Autowired
    private UserdocTypesService userdocTypesService;

    @Test
    @Transactional
    public void removeAllMarkListData() {
        List<String> fileTypes = new ArrayList<>();

        fileTypes.add("S");
        List<CStatus> all = statusService.getAllByFileTypesOrder(fileTypes);


        assertEquals(78L, all.size());
    }

    @Test
    @Transactional
    public void getAllStatusByProcTypesOrderByStatusName() {

        List<String> procTypes = userdocTypesService.getAllProcTypes();
        List<CStatus> all = statusService.getAllByProcessTypesOrder(procTypes);

        assertEquals(377, all.size());
    }
}
