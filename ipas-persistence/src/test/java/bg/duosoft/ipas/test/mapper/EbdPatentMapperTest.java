package bg.duosoft.ipas.test.mapper;

import bg.duosoft.ipas.core.model.ebddownload.CEbdPatent;
import bg.duosoft.ipas.core.model.patent.CAuthorshipData;
import bg.duosoft.ipas.integration.ebddownload.service.EbdPatentService;
import bg.duosoft.ipas.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * User: Georgi
 * Date: 24.2.2020 г.
 * Time: 17:42
 */
public class EbdPatentMapperTest extends TestBase {
    @Autowired
    private EbdPatentService ebdPatentService;
    @Test
    public void testReadPatent() {
        CEbdPatent patent = ebdPatentService.selectByFileNumber(4750871);
        CAuthorshipData authorshipData = patent.getAuthorshipData();
        assertNotNull(authorshipData);

    }
}
